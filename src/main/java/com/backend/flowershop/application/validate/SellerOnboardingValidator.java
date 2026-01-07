package com.backend.flowershop.application.validate;

import com.backend.flowershop.application.context.SellerOnboardingContext;
import com.backend.flowershop.application.validate.pipeline.RulePipeline;
import com.backend.flowershop.application.validate.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：
 * - 卖家入驻校验入口
 *
 * 职责边界：
 * - 负责归一化与调用规则流水线
 * - 不负责具体规则逻辑
 *
 * 使用位置：
 * - 卖家入驻应用服务
 */
@Component
public class SellerOnboardingValidator {

    private final RulePipeline rulePipeline;
    private final List<Rule<SellerOnboardingContext>> rules;

    /**
     * 做什么：
     * - 创建校验器
     *
     * 输入：
     * - rulePipeline：规则流水线
     * - rules：规则列表
     *
     * 输出：
     * - 校验器对象
     */
    public SellerOnboardingValidator(RulePipeline rulePipeline,
                                     List<Rule<SellerOnboardingContext>> rules) {
        this.rulePipeline = rulePipeline;
        this.rules = rules;
    }

    /**
     * 做什么：
     * - 执行卖家入驻校验
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 无
     */
    public void validate(SellerOnboardingContext context) {
        normalize(context);
        rulePipeline.execute(context, rules);
    }

    /**
     * 做什么：
     * - 归一化输入
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 无
     */
    private void normalize(SellerOnboardingContext context) {
        context.setDisplayName(trimToNull(context.getDisplayName()));
        context.setShopName(trimToNull(context.getShopName()));
        context.setDescription(trimToNull(context.getDescription()));
    }

    /**
     * 做什么：
     * - 统一去除字符串空白
     *
     * 输入：
     * - value：原始字符串
     *
     * 输出：
     * - 归一化后的字符串
     */
    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
