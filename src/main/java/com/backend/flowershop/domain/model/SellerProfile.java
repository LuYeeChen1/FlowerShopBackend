package com.backend.flowershop.domain.model;

import java.time.LocalDateTime;

/**
 * 作用：
 * - 表示卖家档案的领域模型
 *
 * 职责边界：
 * - 负责承载卖家档案数据
 * - 不负责持久化与校验
 *
 * 使用位置：
 * - 应用服务与仓储之间
 */
public class SellerProfile {

    private final Long id;
    private final String userId;
    private final String displayName;
    private final String shopName;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private SellerProfile(Long id,
                          String userId,
                          String displayName,
                          String shopName,
                          String description,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.displayName = displayName;
        this.shopName = shopName;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 做什么：
     * - 创建新的卖家档案
     *
     * 输入：
     * - userId：用户 ID
     * - displayName：展示名
     * - shopName：店铺名
     * - description：介绍
     *
     * 输出：
     * - 卖家档案对象
     */
    public static SellerProfile create(String userId, String displayName, String shopName, String description) {
        return new SellerProfile(null, userId, displayName, shopName, description, null, null);
    }

    /**
     * 做什么：
     * - 组装已有的卖家档案
     *
     * 输入：
     * - id：主键
     * - userId：用户 ID
     * - displayName：展示名
     * - shopName：店铺名
     * - description：介绍
     * - createdAt：创建时间
     * - updatedAt：更新时间
     *
     * 输出：
     * - 卖家档案对象
     */
    public static SellerProfile of(Long id,
                                   String userId,
                                   String displayName,
                                   String shopName,
                                   String description,
                                   LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {
        return new SellerProfile(id, userId, displayName, shopName, description, createdAt, updatedAt);
    }

    /**
     * 做什么：
     * - 获取主键 ID
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 主键 ID
     */
    public Long getId() {
        return id;
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
