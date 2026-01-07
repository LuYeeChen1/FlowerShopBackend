package com.backend.flowershop.interfaces.dto;

/**
 * 作用：
 * - 卖家入驻请求 DTO
 *
 * 职责边界：
 * - 负责接收请求数据
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - 卖家入驻控制器
 */
public class SellerOnboardingRequest {

    private String displayName;
    private String shopName;
    private String description;

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
     * - 设置展示名
     *
     * 输入：
     * - displayName：展示名
     *
     * 输出：
     * - 无
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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
     * - 设置店铺名
     *
     * 输入：
     * - shopName：店铺名
     *
     * 输出：
     * - 无
     */
    public void setShopName(String shopName) {
        this.shopName = shopName;
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
     * - 设置介绍
     *
     * 输入：
     * - description：介绍
     *
     * 输出：
     * - 无
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
