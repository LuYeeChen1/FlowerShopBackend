package com.backend.flowershop.interfaceadapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作用：服务存活探针
 * - 不需要 token
 * - 用于确认 Spring Boot 已启动且路由可用
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "ok";
    }
}
