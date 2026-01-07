package com.backend.flowershop.common.exception;

/**
 * 作用：
 * - 表示校验失败的异常
 *
 * 职责边界：
 * - 负责携带校验错误信息
 * - 不负责处理响应
 *
 * 使用位置：
 * - 规则或校验流程中抛出
 */
public class ValidationException extends RuntimeException {

    /**
     * 做什么：
     * - 创建校验异常
     *
     * 输入：
     * - message：错误信息
     *
     * 输出：
     * - 构造后的异常对象
     */
    public ValidationException(String message) {
        super(message);
    }
}
