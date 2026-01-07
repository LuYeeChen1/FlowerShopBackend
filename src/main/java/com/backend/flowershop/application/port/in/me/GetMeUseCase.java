package com.backend.flowershop.application.port.in.me;

import com.backend.flowershop.application.model.result.me.GetMeResult;

/**
 * 作用：GetMeUseCase（获取当前登录用户信息的用例入口）

 * 边界与职责：
 * - 这是 application 层对外暴露的“用例接口”
 * - Controller 只能调用这个接口，不能直接访问 domain / persistence / security 细节

 * 输入/输出：
 * - 输入：无（当前用户由 CurrentUserPort 从安全上下文中解析）
 * - 输出：GetMeResult（application 层结果模型）

 * 约束：
 * - 这里只定义“能做什么”，不包含任何实现
 * - 实现类必须放在 application/service/me/GetMeService.java
 */
public interface GetMeUseCase {

    /**
     * 获取当前登录用户的基础信息。

     * 说明：
     * - 该用例只返回“身份锚点”信息（sub / roleGroup / roleStage 等）
     * - 不负责复杂业务、不做权限修改
     *
     * @return GetMeResult 当前用户信息
     */
    GetMeResult getMe();
}
