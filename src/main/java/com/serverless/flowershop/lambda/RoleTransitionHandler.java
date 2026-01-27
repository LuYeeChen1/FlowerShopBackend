package com.serverless.flowershop.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

/**
 * [AWS Lambda Handler]
 * 职责：接收 API Gateway 的 HTTP 请求，将用户从 CUSTOMER 组移动到 SELLER 组。
 * 触发源：API Gateway (POST /role-transition)
 */
public class RoleTransitionHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;
    private final ObjectMapper objectMapper;

    /**
     * 构造函数
     * Lambda 初始化时会调用一次。在这里初始化耗资源的客户端。
     */
    public RoleTransitionHandler() {
        // 1. 初始化 Cognito 客户端 (使用 Lambda 的执行角色凭证)
        this.cognitoClient = CognitoIdentityProviderClient.create();

        // 2. 初始化 JSON 解析器
        this.objectMapper = new ObjectMapper();

        // 3. 从环境变量读取 User Pool ID
        // 注意：必须在 AWS Lambda 控制台 -> Configuration -> Environment variables 中设置 'USER_POOL_ID'
        this.userPoolId = System.getenv("USER_POOL_ID");

        // 4. 启动时自检 (在 CloudWatch Logs 中可见)
        if (this.userPoolId == null || this.userPoolId.isEmpty()) {
            System.err.println("❌ 严重错误: 环境变量 USER_POOL_ID 未设置！Lambda 将无法正常工作。");
        } else {
            System.out.println("✅ Lambda 初始化成功，目标 User Pool ID: " + this.userPoolId);
        }
    }

    /**
     * 核心处理逻辑
     */
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        // 允许跨域 (CORS) 头，防止前端调用报错
        Map<String, String> headers = Map.of(
                "Content-Type", "application/json",
                "Access-Control-Allow-Origin", "*",
                "Access-Control-Allow-Methods", "POST"
        );
        response.setHeaders(headers);

        try {
            context.getLogger().log("🚀 [Lambda] 开始处理权限切换请求...");

            // 1. 校验 User Pool ID 配置
            if (this.userPoolId == null) {
                throw new IllegalStateException("Missing environment variable: USER_POOL_ID");
            }

            // 2. 解析请求体 (Body)
            String requestBody = request.getBody();
            if (requestBody == null || requestBody.isEmpty()) {
                throw new IllegalArgumentException("Request body is empty");
            }

            // 期待 JSON 格式: { "userId": "us-east-1:xxxx-xxxx..." }
            JsonNode jsonNode = objectMapper.readTree(requestBody);
            if (!jsonNode.has("userId")) {
                throw new IllegalArgumentException("Missing 'userId' field in JSON payload");
            }

            String userId = jsonNode.get("userId").asText();
            context.getLogger().log("🎯 目标用户 ID: " + userId);

            // 3. 执行 Cognito 原子操作

            // A. 尝试移除 'CUSTOMER' 组 (如果存在)
            try {
                adminRemoveUserFromGroup(userId, "CUSTOMER");
                context.getLogger().log("✅ 已成功移除组: CUSTOMER");
            } catch (ResourceNotFoundException e) {
                // 如果用户本来就不在 CUSTOMER 组，这是一个良性情况，不需要报错
                context.getLogger().log("⚠️ 用户不在 CUSTOMER 组中，跳过移除操作。");
            } catch (Exception e) {
                // 其他错误（如权限不足）则记录并抛出
                context.getLogger().log("❌ 移除组失败: " + e.getMessage());
                throw e;
            }

            // B. 强制加入 'SELLER' 组
            adminAddUserToGroup(userId, "SELLER");
            context.getLogger().log("✅ 已成功加入组: SELLER");

            // 4. 返回成功响应 (200 OK)
            response.setStatusCode(200);
            response.setBody("{\"status\": \"SUCCESS\", \"message\": \"User promoted to SELLER successfully\", \"userId\": \"" + userId + "\"}");

        } catch (IllegalArgumentException e) {
            // 客户端参数错误 (400)
            context.getLogger().log("❌ 参数错误: " + e.getMessage());
            response.setStatusCode(400);
            response.setBody("{\"status\": \"BAD_REQUEST\", \"error\": \"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            // 服务器内部错误 (500)
            context.getLogger().log("❌ 系统异常: " + e.getMessage());
            e.printStackTrace(); // 打印完整堆栈到 CloudWatch

            response.setStatusCode(500);
            response.setBody("{\"status\": \"INTERNAL_ERROR\", \"error\": \"" + e.getMessage() + "\"}");
        }

        return response;
    }

    // --- 辅助方法：封装 Cognito SDK 调用 ---

    private void adminRemoveUserFromGroup(String username, String groupName) {
        AdminRemoveUserFromGroupRequest request = AdminRemoveUserFromGroupRequest.builder()
                .userPoolId(this.userPoolId)
                .username(username)
                .groupName(groupName)
                .build();

        cognitoClient.adminRemoveUserFromGroup(request);
    }

    private void adminAddUserToGroup(String username, String groupName) {
        AdminAddUserToGroupRequest request = AdminAddUserToGroupRequest.builder()
                .userPoolId(this.userPoolId)
                .username(username)
                .groupName(groupName)
                .build();

        cognitoClient.adminAddUserToGroup(request);
    }
}