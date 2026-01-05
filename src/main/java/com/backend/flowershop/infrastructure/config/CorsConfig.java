package com.backend.flowershop.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * 作用：解决前端（http://localhost:5173）调用后端（http://localhost:8080）的跨域问题
 * - 允许 Authorization header（Bearer token）
 * - 允许预检 OPTIONS 请求通过
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 只允许你的前端开发域名（不要用 *，因为你要带 Authorization）
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // 允许的方法（至少要 GET + OPTIONS；建议把常用的也放上）
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 允许的请求头（必须包含 Authorization）
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 如果你未来需要读响应头（可选）
        config.setExposedHeaders(List.of("Authorization"));

        // 允许携带凭证（这边其实不靠 cookie，但开着不影响）
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有路径生效
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
