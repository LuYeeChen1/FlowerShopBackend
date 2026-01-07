package com.backend.flowershop.interfaces.controller;

import com.backend.flowershop.interfaces.dto.MeResponse;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 作用：
 * - 提供当前用户信息接口
 *
 * 职责边界：
 * - 负责从认证信息中提取用户数据
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - /me 接口
 */
@RestController
public class MeController {

    /**
     * 做什么：
     * - 返回当前用户信息
     *
     * 输入：
     * - authentication：JWT 认证信息
     *
     * 输出：
     * - 用户信息响应
     */
    @GetMapping("/me")
    public MeResponse me(JwtAuthenticationToken authentication) {
        Jwt jwt = authentication.getToken();
        String userId = jwt.getSubject();
        String username = jwt.getClaimAsString("cognito:username");
        if (username == null) {
            username = jwt.getClaimAsString("username");
        }
        String email = jwt.getClaimAsString("email");
        List<String> groups = jwt.getClaimAsStringList("cognito:groups");
        if (groups == null) {
            groups = Collections.emptyList();
        }
        return new MeResponse(userId, username, email, groups);
    }
}
