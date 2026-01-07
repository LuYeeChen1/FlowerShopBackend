package com.backend.flowershop.application.port.in.seller;

import com.backend.flowershop.application.model.command.seller.SubmitSellerOnboardingCommand;
import com.backend.flowershop.application.model.result.seller.SubmitSellerOnboardingResult;

/**
 * 作用：SubmitSellerOnboardingUseCase（提交 Seller 入驻申请用例入口）

 * 边界与职责：
 * - 定义“Customer 申请成为 Seller”的应用层用例接口
 * - Controller 只能调用此接口，不能直接操作 domain / persistence / security

 * 输入/输出：
 * - 输入：SubmitSellerOnboardingCommand（已做 Normalize/Validator 前的原始命令）
 * - 输出：SubmitSellerOnboardingResult（提交结果，只读）

 * 约束：
 * - 这里只定义“能做什么”，不包含任何实现
 * - 实现类必须放在 application/service/seller/SubmitSellerOnboardingService.java
 * - 权限与状态判断必须通过 rules / pipeline / validator 完成，不能写在 Controller
 */
public interface SubmitSellerOnboardingUseCase {

    /**
     * 提交 Seller 入驻申请。

     * 业务语义：
     * - 仅允许当前 roleGroup= CUSTOMER
     * - 仅允许当前 roleStage= CUSTOMER
     * - 提交后进入 SELLER_PENDING（或后续阶段由规则决定）
     *
     * @param command 入驻申请命令
     * @return SubmitSellerOnboardingResult 提交结果
     */
    SubmitSellerOnboardingResult submit(SubmitSellerOnboardingCommand command);
}
