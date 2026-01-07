package com.backend.flowershop.application.context;

/**
 * 作用：
 * - 承载卖家入驻业务上下文
 *
 * 职责边界：
 * - 负责保存当前请求所需数据
 * - 不负责校验与持久化
 *
 * 使用位置：
 * - 控制器 -> 校验 -> 规则流水线
 */
public class SellerOnboardingContext {

    private String userId;
    private String username;
    private String email;
    private String displayName;
    private String shopName;
    private String description;
    private OperationType operationType;

    /**
     * 做什么：
     * - 创建空的上下文
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 上下文对象
     */
    public SellerOnboardingContext() {
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
     * - 设置用户 ID
     *
     * 输入：
     * - userId：用户 ID
     *
     * 输出：
     * - 无
     */
    public void setUserId(String userId) {
        this.userId = userId;
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
     * - 设置用户名
     *
     * 输入：
     * - username：用户名
     *
     * 输出：
     * - 无
     */
    public void setUsername(String username) {
        this.username = username;
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
     * - 设置邮箱
     *
     * 输入：
     * - email：邮箱
     *
     * 输出：
     * - 无
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     * 做什么：
     * - 获取操作类型
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 操作类型
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * 做什么：
     * - 设置操作类型
     *
     * 输入：
     * - operationType：操作类型
     *
     * 输出：
     * - 无
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
