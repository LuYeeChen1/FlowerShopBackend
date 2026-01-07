package com.backend.flowershop.application.validate.rules;

/**
 * 作用：
 * - 定义业务校验规则接口
 *
 * 职责边界：
 * - 负责约束规则执行入口
 * - 不负责规则编排
 *
 * 使用位置：
 * - 规则实现类与规则流水线
 */
public interface Rule<C> {

    /**
     * 做什么：
     * - 执行规则校验
     *
     * 输入：
     * - context：业务上下文
     *
     * 输出：
     * - 无（失败则抛异常）
     */
    void apply(C context);
}
