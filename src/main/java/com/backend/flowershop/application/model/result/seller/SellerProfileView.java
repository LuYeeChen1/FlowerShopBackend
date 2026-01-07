package com.backend.flowershop.application.model.result.seller;

import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.common.Email;
import com.backend.flowershop.domain.model.common.PhoneNumber;
import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerId;
import com.backend.flowershop.domain.model.seller.SellerStage;

/**
 * 作用：SellerProfileView（Seller 资料只读视图）

 * 边界与职责：
 * - 用于 application 层向 interfaces 层暴露 Seller 资料
 * - 这是“查询结果视图”，不是 Domain Model

 * 设计说明：
 * - 使用 record：只读、不可变、语义明确
 * - 字段全部来自 domain 值对象，避免 primitive 污染
 * - 不包含任何业务行为或状态修改逻辑

 * 包含信息：
 * - sellerId：Seller 唯一标识
 * - stage：当前 Seller 所处阶段
 * - email：联系邮箱
 * - phoneNumber：联系电话
 * - address：地址信息
 * - document：Seller 提交的文件信息
 */
public record SellerProfileView(
        SellerId sellerId,
        SellerStage stage,
        Email email,
        PhoneNumber phoneNumber,
        Address address,
        SellerDocument document
) {
}
