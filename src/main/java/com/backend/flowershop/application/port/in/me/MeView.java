package com.backend.flowershop.application.port.in.me;

import com.backend.flowershop.domain.model.user.CognitoSub;
import com.backend.flowershop.domain.model.user.RoleGroup;
import com.backend.flowershop.domain.model.user.RoleStage;

/**
 * 作用：MeView（当前用户信息的用例视图接口）

 * 边界与职责：
 * - 定义“GetMe 用例”对外暴露的最小视图契约
 * - 用于 application 层向 interfaces 层提供稳定的数据形态

 * 设计说明：
 * - 这是 View（只读投影），不是 Domain Model
 * - 与 GetMeResult 不同：View 面向接口层使用，Result 面向用例内部/测试
 * - 通过接口而非具体实现，避免接口层直接依赖实现细节

 * 包含信息：
 * - sub：Cognito 用户唯一标识
 * - roleGroup：权限面角色（ADMIN / SELLER / CUSTOMER）
 * - roleStage：状态机阶段（CUSTOMER / SELLER_PENDING / ...）
 */
public interface MeView {

    CognitoSub sub();

    RoleGroup roleGroup();

    RoleStage roleStage();
}
