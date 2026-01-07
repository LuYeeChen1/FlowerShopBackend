package com.backend.flowershop.application.model.command.seller;

import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.common.Email;
import com.backend.flowershop.domain.model.common.PhoneNumber;
import com.backend.flowershop.domain.model.seller.SellerDocument;

/**
 * 作用：UpdateSellerProfileCommand（更新 Seller 资料命令）

 * 边界与职责：
 * - 表达“Seller 想更新自己的资料”
 * - 作为 application 层接收接口层输入的标准命令对象

 * 设计说明：
 * - 使用 record：不可变、语义清晰
 * - 不包含任何校验或权限逻辑
 * - 具体哪些字段允许更新，由 validator / rules / pipeline 决定

 * 包含信息：
 * - email：更新后的联系邮箱（可选/是否允许由规则决定）
 * - phoneNumber：更新后的联系电话
 * - address：更新后的地址信息
 * - document：更新或补充的 Seller 文件
 */
public record UpdateSellerProfileCommand(
        Email email,
        PhoneNumber phoneNumber,
        Address address,
        SellerDocument document
) {
}
