package com.backend.flowershop.interfaceadapter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作用：Step 1 验证前端能否调用到后端
 * - 先返回 JSON，避免前端 resp.json() 解析失败
 * - 这里暂时不提取 sub/email、不写库（避免跳步）
 */
@RestController
public class MeController {

    @GetMapping("/me")
    public MeResponse me() {
        return new MeResponse("ok");
    }

    public record MeResponse(String message) {}
}
