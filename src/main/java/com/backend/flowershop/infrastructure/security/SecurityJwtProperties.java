package com.backend.flowershop.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 作用：
 * - 绑定 JWT 相关配置
 *
 * 职责边界：
 * - 负责读取配置属性
 * - 不负责安全逻辑
 *
 * 使用位置：
 * - 安全配置类
 */
@ConfigurationProperties(prefix = "security.jwt")
public class SecurityJwtProperties {

    private String issuer;
    private String clientId;

    /**
     * 做什么：
     * - 获取 issuer
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - issuer
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * 做什么：
     * - 设置 issuer
     *
     * 输入：
     * - issuer：发行者
     *
     * 输出：
     * - 无
     */
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    /**
     * 做什么：
     * - 获取 clientId
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * 做什么：
     * - 设置 clientId
     *
     * 输入：
     * - clientId：客户端 ID
     *
     * 输出：
     * - 无
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
