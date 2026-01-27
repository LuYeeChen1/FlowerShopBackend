package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter; // 必须引入
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. 公开接口
                        .requestMatchers("/api/public/**", "/api/auth/**").permitAll()

                        // ============================================================
                        // 🔥 核心修复点：将“申请”和“状态查询”特例化，放在通配符之前！
                        // ============================================================

                        // 允许 CUSTOMER (或所有登录用户) 访问申请接口
                        .requestMatchers("/api/seller/apply").hasAnyRole("CUSTOMER", "SELLER")

                        // 允许 CUSTOMER 查看申请状态 (否则他们不知道自己通过没)
                        .requestMatchers("/api/seller/status").hasAnyRole("CUSTOMER", "SELLER")

                        // ============================================================
                        // 🔒 剩下的 /api/seller/** 依然必须是 SELLER 才能访问
                        // (例如：上架商品、查看订单等)
                        // ============================================================
                        .requestMatchers("/api/seller/**").hasRole("SELLER")

                        // 其他业务接口
                        .requestMatchers("/api/cart/**", "/api/orders/**").authenticated()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // 使用您编写的 JwtRoleConverter
        converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());
        return converter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH" , "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}