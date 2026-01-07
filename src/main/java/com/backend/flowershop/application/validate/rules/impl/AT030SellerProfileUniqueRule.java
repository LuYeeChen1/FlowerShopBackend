package com.backend.flowershop.application.validate.rules.impl;

import com.backend.flowershop.application.context.OperationType;
import com.backend.flowershop.application.context.SellerOnboardingContext;
import com.backend.flowershop.application.port.SellerProfileRepository;
import com.backend.flowershop.application.validate.rules.Rule;
import com.backend.flowershop.common.exception.ValidationException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 作用：
 * - 校验卖家档案唯一性
 *
 * 职责边界：
 * - 负责用户档案是否已存在
 * - 不负责创建或更新
 *
 * 使用位置：
 * - 卖家入驻规则流水线
 */
@Component
@Order(30)
public class AT030SellerProfileUniqueRule implements Rule<SellerOnboardingContext> {

    private final SellerProfileRepository repository;

    /**
     * 做什么：
     * - 创建唯一性校验规则
     *
     * 输入：
     * - repository：卖家档案仓储
     *
     * 输出：
     * - 规则对象
     */
    public AT030SellerProfileUniqueRule(SellerProfileRepository repository) {
        this.repository = repository;
    }

    /**
     * 做什么：
     * - 校验提交场景下用户档案唯一
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 无
     */
    @Override
    public void apply(SellerOnboardingContext context) {
        if (context.getOperationType() != OperationType.SUBMIT) {
            return;
        }
        if (repository.findByUserId(context.getUserId()).isPresent()) {
            throw new ValidationException("卖家档案已存在");
        }
    }
}
