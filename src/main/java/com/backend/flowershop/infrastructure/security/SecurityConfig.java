package com.backend.flowershop.infrastructure.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 作用：
 * - 配置资源服务器安全策略
 *
 * 职责边界：
 * - 负责 JWT 校验与接口权限
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - Spring Security 配置
 */
@Configuration
public class SecurityConfig {

    /**
     * 做什么：
     * - 配置安全过滤链
     *
     * 输入：
     * - http：安全配置对象
     *
     * 输出：
     * - 安全过滤链
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/health").permitAll()
                .anyRequest().authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

    /**
     * 做什么：
     * - 配置 JWT 解码器与校验器
     *
     * 输入：
     * - properties：资源服务器配置
     * - jwtProperties：自定义 JWT 配置
     *
     * 输出：
     * - JWT 解码器
     */
    @Bean
    public JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties,
                                 SecurityJwtProperties jwtProperties) {
        String jwkSetUri = properties.getJwt().getJwkSetUri();
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
        OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators.createDefaultWithIssuer(jwtProperties.getIssuer());
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(jwtProperties.getClientId());
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(issuerValidator, audienceValidator);
        decoder.setJwtValidator(validator);
        return decoder;
    }
}
