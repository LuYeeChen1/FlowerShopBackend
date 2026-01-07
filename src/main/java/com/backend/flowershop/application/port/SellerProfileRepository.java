package com.backend.flowershop.application.port;

import com.backend.flowershop.domain.model.SellerProfile;

import java.util.Optional;

/**
 * 作用：
 * - 定义卖家档案仓储端口
 *
 * 职责边界：
 * - 负责数据访问抽象
 * - 不负责具体实现
 *
 * 使用位置：
 * - 应用服务与规则
 */
public interface SellerProfileRepository {

    /**
     * 做什么：
     * - 保存卖家档案
     *
     * 输入：
     * - profile：卖家档案
     *
     * 输出：
     * - 保存后的档案
     */
    SellerProfile save(SellerProfile profile);

    /**
     * 做什么：
     * - 根据用户 ID 查询卖家档案
     *
     * 输入：
     * - userId：用户 ID
     *
     * 输出：
     * - 可选的档案
     */
    Optional<SellerProfile> findByUserId(String userId);

    /**
     * 做什么：
     * - 根据用户 ID 更新卖家档案
     *
     * 输入：
     * - profile：卖家档案
     *
     * 输出：
     * - 是否更新成功
     */
    boolean updateByUserId(SellerProfile profile);
}
