package com.backend.flowershop.application.model.result.seller;

import com.backend.flowershop.domain.model.seller.SellerId;
import com.backend.flowershop.domain.model.seller.SellerStage;

/**
 * 作用：SubmitSellerOnboardingResult（提交 Seller 入驻申请结果）

 * 边界与职责：
 * - 表达“提交入驻申请之后，系统给出的结果”
 * - 作为 application 层返回给接口层（Controller）的只读结果模型

 * 设计说明：
 * - 使用 record：不可变、无行为、只承载结果数据
 * - 不包含任何业务逻辑或状态变更行为

 * 包含信息：
 * - sellerId：系统为该 Seller 生成的唯一标识
 * - sellerStage：提交后的 Seller 状态阶段（通常为 SELLER_PENDING）
 */
public record SubmitSellerOnboardingResult(
        SellerId sellerId,
        SellerStage sellerStage
) {
}
