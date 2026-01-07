package com.backend.flowershop.infrastructure.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * 作用：
 * - 校验 JWT audience
 *
 * 职责边界：
 * - 负责 aud 或 client_id 校验
 * - 不负责认证流程
 *
 * 使用位置：
 * - JWT 解码器配置
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String clientId;

    /**
     * 做什么：
     * - 创建 audience 校验器
     *
     * 输入：
     * - clientId：客户端 ID
     *
     * 输出：
     * - 校验器对象
     */
    public AudienceValidator(String clientId) {
        this.clientId = clientId;
    }

    /**
     * 做什么：
     * - 校验 token 是否包含 clientId
     *
     * 输入：
     * - jwt：JWT token
     *
     * 输出：
     * - 校验结果
     */
    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (clientId == null || clientId.isBlank()) {
            return OAuth2TokenValidatorResult.success();
        }
        List<String> audience = jwt.getAudience();
        if (audience != null && audience.contains(clientId)) {
            return OAuth2TokenValidatorResult.success();
        }
        Object claim = jwt.getClaims().get("client_id");
        if (clientId.equals(claim)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error("invalid_token", "clientId 校验失败", null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
