package com.backend.flowershop.application.model.result.me;

import com.backend.flowershop.domain.model.user.CognitoSub;
import com.backend.flowershop.domain.model.user.RoleGroup;
import com.backend.flowershop.domain.model.user.RoleStage;

/**
 * 作用：GetMeResult（获取当前登录用户信息的应用层结果模型）

 * 边界与职责：
 * - 用于 application 层向 interfaces 层返回“当前用户是谁”的结果
 * - 这是只读结果模型，不包含任何业务行为

 * 设计说明：
 * - 使用 record：不可变、语义清晰、适合结果返回
 * - 字段全部来自 domain 的值对象/枚举，避免 primitive 滥用

 * 字段含义：
 * - sub：Cognito 用户唯一标识
 * - roleGroup：权限面角色（ADMIN / SELLER / CUSTOMER）
 * - roleStage：状态机阶段（CUSTOMER / SELLER_PENDING / ... / SELLER_ACTIVE）
 */
public record GetMeResult(
        CognitoSub sub,
        RoleGroup roleGroup,
        RoleStage roleStage
) {
}
