package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 作用：只负责安全“守门”
 * - /health：匿名可访问
 * - /me：必须携带 Bearer access_token
 * - JWT 校验：由 Spring OAuth2 Resource Server 完成（验签 + iss + exp）
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 说明：本项目是纯后端 REST API，禁用 CSRF 更符合 API 场景
        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                // 健康检查放行
                .requestMatchers("/health").permitAll()
                // /me 必须登录（携带有效 access_token）
                .requestMatchers("/me").authenticated()
                // 其他路径：先全部放行（避免你在 Step 1 被挡住）
                .anyRequest().permitAll()
        );

        // 说明：启用 OAuth2 Resource Server（JWT）
        // - 会自动根据 application.yml 的 issuer-uri 找到 JWK 公钥并验签
        // - 同时校验 iss 与 exp（默认行为）
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        return http.build();
    }
}
