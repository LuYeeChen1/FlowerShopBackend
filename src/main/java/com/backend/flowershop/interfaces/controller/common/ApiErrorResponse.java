package com.backend.flowershop.interfaces.controller.common;

import java.time.Instant;
import java.util.List;

/**
 * 作用：统一错误响应结构（对外输出）
 * 边界：只承载错误信息，不做业务逻辑
 */
public record ApiErrorResponse(
        String code,
        String message,
        Instant timestamp,
        List<FieldError> errors
) {
    public record FieldError(
            String field,
            String code,
            String message
    ) {}
}
