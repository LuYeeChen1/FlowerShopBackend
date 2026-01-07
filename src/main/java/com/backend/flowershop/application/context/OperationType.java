package com.backend.flowershop.application.context;

/**
 * 作用：
 * - 表示当前业务操作类型
 *
 * 职责边界：
 * - 负责标识操作场景
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - 卖家入驻上下文
 */
public enum OperationType {
    SUBMIT,
    UPDATE
}
