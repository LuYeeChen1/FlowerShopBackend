package com.backend.flowershop.application.model.result.seller;

import com.backend.flowershop.domain.model.seller.SellerId;
import com.backend.flowershop.domain.model.seller.SellerStage;

/**
 * 作用：UpdateSellerProfileResult（更新 Seller 资料结果）

 * 边界与职责：
 * - 表达 Seller 资料更新完成后的系统返回结果
 * - 作为 application 层返回给接口层（Controller）的只读结果模型

 * 设计说明：
 * - 使用 record：不可变、语义清晰，仅承载结果数据
 * - 不包含任何业务逻辑或状态变更行为

 * 包含信息：
 * - sellerId：Seller 的唯一标识
 * - sellerStage：更新完成后的 Seller 当前阶段
 *   （阶段是否变化由业务规则决定，而不是由 Result 决定）
 */
public record UpdateSellerProfileResult(
        SellerId sellerId,
        SellerStage sellerStage
) {
}
