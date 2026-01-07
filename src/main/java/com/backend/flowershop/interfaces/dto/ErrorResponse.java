package com.backend.flowershop.interfaces.dto;

/**
 * 作用：
 * - 统一错误响应结构
 *
 * 职责边界：
 * - 负责承载错误信息
 * - 不负责异常处理逻辑
 *
 * 使用位置：
 * - 全局异常处理器返回
 */
public class ErrorResponse {

    private final String message;

    /**
     * 做什么：
     * - 创建错误响应
     *
     * 输入：
     * - message：错误提示
     *
     * 输出：
     * - 错误响应对象
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * 做什么：
     * - 获取错误信息
     *
     * 输入：
     * - 无
     *
     * 输出：
     * - 错误信息
     */
    public String getMessage() {
        return message;
    }
}
