package com.backend.flowershop.interfaces.advice;

import com.backend.flowershop.common.exception.ValidationException;
import com.backend.flowershop.interfaces.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 作用：
 * - 统一处理接口层异常
 *
 * 职责边界：
 * - 负责异常到响应的映射
 * - 不负责业务逻辑
 *
 * 使用位置：
 * - 接口层异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 做什么：
     * - 处理校验异常
     *
     * 输入：
     * - ex：校验异常
     *
     * 输出：
     * - 400 错误响应
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
