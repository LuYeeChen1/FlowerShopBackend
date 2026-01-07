package com.backend.flowershop.application.port.out.persistence;

import com.backend.flowershop.domain.model.seller.SellerId;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import com.backend.flowershop.domain.model.user.CognitoSub;

import java.util.Optional;

/**
 * 作用：SellerProfileRepository（Seller 资料持久化出口 Port）

 * 边界与职责：
 * - application 层通过该 Port 读写 SellerProfile
 * - 明确区分「创建」与「更新」，避免语义混淆
 * - 不关心底层是 MySQL / DynamoDB / 其他存储

 * 设计原则：
 * - create ≠ update：由用例/服务层明确意图
 * - Repository 不做“是否存在”的业务判断

 * 约束：
 * - 这是能力声明接口，不包含任何实现
 * - 具体实现必须放在 infrastructure/persistence/* 下
 * - Domain 层不允许依赖 Repository
 */
public interface SellerProfileRepository {

    /**
     * 根据 SellerId 查询 SellerProfile。
     *
     * @param sellerId Seller 唯一标识
     * @return Optional<SellerProfile>
     */
    Optional<SellerProfile> findById(SellerId sellerId);

    /**
     * 根据 CognitoSub 查询 SellerProfile。

     * 说明：
     * - 一个 Cognito 用户最多对应一个 SellerProfile
     *
     * @param sub Cognito 用户唯一标识
     * @return Optional<SellerProfile>
     */
    Optional<SellerProfile> findBySub(CognitoSub sub);

    /**
     * 创建新的 SellerProfile。

     * 语义约束：
     * - 仅用于「首次提交 Seller Onboarding」
     * - 若已存在对应记录，具体行为（异常/忽略）由实现层决定
     *
     * @param profile 新的 SellerProfile
     */
    void create(SellerProfile profile);

    /**
     * 更新已存在的 SellerProfile。

     * 语义约束：
     * - 仅用于 Seller 已存在的前提下
     * - 不负责检查是否存在（由上层保证）
     *
     * @param profile 已存在的 SellerProfile
     */
    void update(SellerProfile profile);
}
