package com.backend.flowershop.application.model.command.seller;

import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.common.Email;
import com.backend.flowershop.domain.model.common.PhoneNumber;
import com.backend.flowershop.domain.model.seller.SellerDocument;

/**
 * 作用：SubmitSellerOnboardingCommand（提交 Seller 入驻申请命令）

 * 边界与职责：
 * - 表达“用户想做什么”，而不是“系统如何做”
 * - 作为 application 层接收 Controller 输入的标准命令对象

 * 设计说明：
 * - 使用 record：不可变、语义清晰
 * - 不包含任何校验逻辑（校验必须放在 validator / rules）
 * - 字段类型优先使用 domain 值对象，避免 primitive 污染

 * 包含信息：
 * - email：Seller 联系邮箱
 * - phoneNumber：Seller 联系电话
 * - address：Seller 地址信息
 * - document：Seller 提交的身份/营业相关文件
 */
public record SubmitSellerOnboardingCommand(
        Email email,
        PhoneNumber phoneNumber,
        Address address,
        SellerDocument document
) {
}
