package com.backend.flowershop.interfaces.dto;

import java.util.List;

/**
 * 作用：
 * - 当前用户信息响应 DTO
 *
 * 职责边界：
 * - 负责返回用户信息
 * - 不负责认证逻辑
 *
 * 使用位置：
 * - /me 接口
 */
public class MeResponse {

    private final String userId;
    private final String username;
    private final String email;
    private final List<String> groups;

    /**
     * 做什么：
     * - 创建用户信息响应
     *
     * 输入：
     * - userId：用户 ID
     * - username：用户名
     * - email：邮箱
     * - groups：用户组
     *
     * 输出：
     * - 响应对象
     */
    public MeResponse(String userId, String username, String email, List<String> groups) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.groups = groups;
    }

    /**
     * 做什么：
     * - 获取用户 ID
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 用户 ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 做什么：
     * - 获取用户名
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 做什么：
     * - 获取邮箱
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 做什么：
     * - 获取用户组
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 用户组列表
     */
    public List<String> getGroups() {
        return groups;
    }
}
