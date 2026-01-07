package com.backend.flowershop.application.port.out.security;

import com.backend.flowershop.domain.model.user.CognitoSub;
import com.backend.flowershop.domain.model.user.RoleGroup;
import com.backend.flowershop.domain.model.user.RoleStage;

/**
 * 作用：CurrentUserPort（当前登录用户出口 Port）

 * 边界与职责：
 * - application 层需要“知道当前是谁在调用”，但不能依赖 Spring Security / JWT / HTTP 细节
 * - 因此通过这个 Port 抽象出“当前用户信息”的读取

 * 输入/输出：
 * - 输入：无（由实现层从安全上下文/JWT 中解析）
 * - 输出：当前用户的关键身份信息（sub / role_group / role_stage）

 * 约束：
 * - 这里只定义“需要什么”，不关心“怎么拿到”
 * - 具体实现必须放在 infrastructure/security/CurrentUserAdapter.java
 */
public interface CurrentUserPort {

    /**
     * 读取当前用户在 Cognito 中的唯一标识（sub）。
     *
     * @return CognitoSub（不可为 null）
     */
    CognitoSub currentSub();

    /**
     * 读取当前用户所属的主角色组（来自 Cognito Groups 映射）。

     * 说明：
     * - 这是“权限面”的角色：ADMIN / SELLER / CUSTOMER
     * - 具体从哪个 JWT Claim 解析，由 Adapter 决定
     *
     * @return RoleGroup（不可为 null）
     */
    RoleGroup currentRoleGroup();

    /**
     * 读取当前用户的 role_stage（来自 custom:role_stage）。

     * 说明：
     * - 这是“状态机面”的角色阶段：CUSTOMER / SELLER_PENDING / ... / SELLER_ACTIVE
     * - 业务用例可用它做规则判断（例如是否允许提交 onboarding）
     *
     * @return RoleStage（不可为 null）
     */
    RoleStage currentRoleStage();
}
