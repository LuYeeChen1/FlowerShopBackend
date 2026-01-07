package com.backend.flowershop.application.port.in.seller;

import com.backend.flowershop.application.model.result.seller.SellerProfileView;

/**
 * 作用：GetSellerProfileUseCase（获取 Seller 资料用例入口）

 * 边界与职责：
 * - 定义“读取 Seller 资料”的应用层用例接口
 * - 仅用于查询，不产生任何状态变更
 * - Controller 只能调用该接口，不能直接访问 persistence 或 domain

 * 输入/输出：
 * - 输入：无（Seller 身份由 CurrentUserPort 从安全上下文中解析）
 * - 输出：SellerProfileView（只读视图）

 * 约束：
 * - 这里只定义用例能力，不包含任何实现
 * - 实现类必须放在 application/service/seller/GetSellerProfileService.java
 */
public interface GetSellerProfileUseCase {

    /**
     * 获取当前登录 Seller 的资料信息。

     * 业务语义：
     * - 仅允许已存在 SellerProfile 的用户调用
     * - 权限与阶段校验由规则/授权层负责
     *
     * @return SellerProfileView Seller 资料视图
     */
    SellerProfileView getProfile();
}
