package com.backend.flowershop.application.validate.rules.impl;

import com.backend.flowershop.application.context.SellerOnboardingContext;
import com.backend.flowershop.application.validate.rules.Rule;
import com.backend.flowershop.common.exception.ValidationException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 作用：
 * - 校验卖家基本信息必填
 *
 * 职责边界：
 * - 负责必填校验
 * - 不负责长度或唯一性校验
 *
 * 使用位置：
 * - 卖家入驻规则流水线
 */
@Component
@Order(10)
public class AT010SellerProfileRequiredRule implements Rule<SellerOnboardingContext> {

    /**
     * 做什么：
     * - 校验必填字段
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 无
     */
    @Override
    public void apply(SellerOnboardingContext context) {
        if (context.getDisplayName() == null) {
            throw new ValidationException("displayName 必填");
        }
        if (context.getShopName() == null) {
            throw new ValidationException("shopName 必填");
        }
    }
}
