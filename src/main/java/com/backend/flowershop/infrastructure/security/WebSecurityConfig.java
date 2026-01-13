package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. ✅ 新增：启用 CORS (允许前端跨域访问)
                .cors(Customizer.withDefaults())

                // 2. 禁用 CSRF (REST API 不需要，且由 JWT 保证安全)
                .csrf(csrf -> csrf.disable())

                // 3. 配置路径权限
                .authorizeHttpRequests(auth -> auth
                        // 🔓 允许所有访客访问 /api/public/ 下的接口
                        .requestMatchers("/api/public/**").permitAll()
                        // 🔒 其他接口 (如 /api/users/me) 必须携带 Cognito Token
                        .anyRequest().authenticated()
                )

                // 4. 启用 OAuth2 资源服务器 (解析 Cognito JWT)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    /**
     * ✅ 核心配置：定义具体的 CORS 规则
     * 允许前端 (localhost:5173) 访问后端的所有接口
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许的前端地址 (Vue 默认端口)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // 允许的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 允许的 Header (关键是 Authorization，用于带 Token)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 是否允许携带凭证 (可选，但在某些复杂认证场景下需要)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}