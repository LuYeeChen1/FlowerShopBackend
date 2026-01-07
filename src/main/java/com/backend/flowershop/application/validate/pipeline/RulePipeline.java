package com.backend.flowershop.application.validate.pipeline;

import com.backend.flowershop.application.validate.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：
 * - 统一执行规则流水线
 *
 * 职责边界：
 * - 负责按顺序执行规则
 * - 不负责规则内容
 *
 * 使用位置：
 * - 校验器调用
 */
@Component
public class RulePipeline {

    /**
     * 做什么：
     * - 按顺序执行规则列表
     *
     * 输入：
     * - context：业务上下文
     * - rules：规则列表
     *
     * 输出：
     * - 无
     */
    public <C> void execute(C context, List<Rule<C>> rules) {
        for (Rule<C> rule : rules) {
            rule.apply(context);
        }
    }
}
