package com.backend.flowershop.interfaces.dto;

import java.time.LocalDateTime;

/**
 * 作用：
 * - 卖家入驻响应 DTO
 *
 * 职责边界：
 * - 负责返回卖家档案信息
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - 卖家入驻控制器
 */
public class SellerOnboardingResponse {

    private final String userId;
    private final String displayName;
    private final String shopName;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    /**
     * 做什么：
     * - 创建响应对象
     *
     * 输入：
     * - userId：用户 ID
     * - displayName：展示名
     * - shopName：店铺名
     * - description：介绍
     * - createdAt：创建时间
     * - updatedAt：更新时间
     *
     * 输出：
     * - 响应对象
     */
    public SellerOnboardingResponse(String userId,
                                    String displayName,
                                    String shopName,
                                    String description,
                                    LocalDateTime createdAt,
                                    LocalDateTime updatedAt) {
        this.userId = userId;
        this.displayName = displayName;
        this.shopName = shopName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
     * - 获取展示名
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 展示名
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 做什么：
     * - 获取店铺名
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 店铺名
     */
    public String getShopName() {
        return shopName;
    }

    /**
     * 做什么：
     * - 获取介绍
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 介绍
     */
    public String getDescription() {
        return description;
    }

    /**
     * 做什么：
     * - 获取创建时间
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 创建时间
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * 做什么：
     * - 获取更新时间
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 更新时间
     */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
