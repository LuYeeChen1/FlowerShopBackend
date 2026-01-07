package com.backend.flowershop.application.validate.rules.impl;

import com.backend.flowershop.application.context.SellerOnboardingContext;
import com.backend.flowershop.application.validate.rules.Rule;
import com.backend.flowershop.common.exception.ValidationException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 作用：
 * - 校验卖家信息长度
 *
 * 职责边界：
 * - 负责字符串长度规则
 * - 不负责必填或唯一性
 *
 * 使用位置：
 * - 卖家入驻规则流水线
 */
@Component
@Order(20)
public class AT020SellerProfileLengthRule implements Rule<SellerOnboardingContext> {

    private static final int DISPLAY_NAME_MAX = 50;
    private static final int SHOP_NAME_MAX = 80;
    private static final int DESCRIPTION_MAX = 500;

    /**
     * 做什么：
     * - 校验字段长度
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 无
     */
    @Override
    public void apply(SellerOnboardingContext context) {
        if (context.getDisplayName() != null && context.getDisplayName().length() > DISPLAY_NAME_MAX) {
            throw new ValidationException("displayName 太长");
        }
        if (context.getShopName() != null && context.getShopName().length() > SHOP_NAME_MAX) {
            throw new ValidationException("shopName 太长");
        }
        if (context.getDescription() != null && context.getDescription().length() > DESCRIPTION_MAX) {
            throw new ValidationException("description 太长");
        }
    }
}
