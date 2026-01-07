package com.backend.flowershop.application.port.in.seller;

import com.backend.flowershop.application.model.command.seller.UpdateSellerProfileCommand;
import com.backend.flowershop.application.model.result.seller.UpdateSellerProfileResult;

/**
 * 作用：UpdateSellerProfileUseCase（更新 Seller 资料用例入口）

 * 边界与职责：
 * - 定义 Seller 在入驻流程中或成为 Seller 后，更新自身资料的用例接口
 * - Controller 只能调用该接口，不得直接访问 domain / persistence / security

 * 输入/输出：
 * - 输入：UpdateSellerProfileCommand（更新资料命令）
 * - 输出：UpdateSellerProfileResult（更新后的结果）

 * 约束：
 * - 这里只定义“能做什么”，不包含任何实现
 * - 实现类必须放在 application/service/seller/UpdateSellerProfileService.java
 * - 权限与阶段校验必须通过 rules / pipeline / validator 完成
 */
public interface UpdateSellerProfileUseCase {

    /**
     * 更新 Seller 的资料信息。

     * 业务语义：
     * - 仅允许当前用户已存在 SellerProfile
     * - 不同 SellerStage 允许更新的字段由规则层决定
     *
     * @param command 更新 Seller 资料命令
     * @return UpdateSellerProfileResult 更新结果
     */
    UpdateSellerProfileResult update(UpdateSellerProfileCommand command);
}
