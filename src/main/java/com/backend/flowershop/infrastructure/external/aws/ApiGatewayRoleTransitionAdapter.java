package com.backend.flowershop.infrastructure.external.aws;

import com.backend.flowershop.application.port.out.RoleTransitionPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient; // Spring Boot 3.2+ 推荐，旧版可用 RestTemplate

import java.util.Map;

/**
 * [Clean Architecture - Infrastructure Adapter]
 * 职责：具体的 HTTP 实现，调用 AWS API Gateway 触发 Lambda。
 */
@Component
public class ApiGatewayRoleTransitionAdapter implements RoleTransitionPort {

    private final RestClient restClient;

    // 从 application.properties 读取 AWS API Gateway 的 URL
    @Value("${aws.api-gateway.role-transition-url}")
    private String apiGatewayUrl;

    public ApiGatewayRoleTransitionAdapter() {
        this.restClient = RestClient.create();
    }

    @Override
    public void promoteToSeller(String userId) {
        // 1. 安全检查：如果 URL 没配置（比如在本地测试环境），就跳过，防止报错
        if (apiGatewayUrl == null || apiGatewayUrl.isEmpty()) {
            System.err.println("⚠️ [AWS Adapter] API Gateway URL 未配置，跳过云端同步。");
            return;
        }

        try {
            System.out.println("🔄 [AWS Adapter] 正在请求 API Gateway 提升权限: " + userId);

            // 2. 发送 POST 请求
            // Payload 格式: { "userId": "..." }
            String response = restClient.post()
                    .uri(apiGatewayUrl)
                    .body(Map.of("userId", userId))
                    .retrieve()
                    .body(String.class);

            System.out.println("✅ [AWS Adapter] 云端响应成功: " + response);

        } catch (Exception e) {
            // 3. 容错处理 (Fire-and-Forget)
            // 即使云端调用失败，也不要回滚本地数据库的 ACTIVE 状态，否则用户会莫名其妙失败。
            // 生产环境建议：将失败的 userId 写入日志或死信队列 (DLQ) 后续重试。
            System.err.println("❌ [AWS Adapter] 调用失败: " + e.getMessage());
        }
    }
}