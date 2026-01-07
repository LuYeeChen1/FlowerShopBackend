package com.backend.flowershop.interfaces.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作用：
 * - 提供健康检查接口
 *
 * 职责边界：
 * - 负责返回健康状态
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - /health 接口
 */
@RestController
public class HealthController {

    /**
     * 做什么：
     * - 返回健康状态
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 200 OK
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
