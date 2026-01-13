package com.backend.flowershop.application.dto.response;

// 用于返回给前端的用户信息
public record UserDTOResponse(
        String id,
        String email,
        String username,
        String role
        // 未来可以在这里加 avatarUrl, lastLoginTime 等
) {}