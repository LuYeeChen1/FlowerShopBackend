package com.backend.flowershop.interfaces.controller.common;

import com.backend.flowershop.application.validator.ValidationError;
import com.backend.flowershop.domain.error.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 作用：统一异常处理（最小实现）
 * - DomainException -> 400
 * - 无效 token -> 401
 * - 无权限 -> 403
 * - 其他 -> 500

 * 边界：只做错误响应映射，不做业务逻辑
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomain(DomainException ex) {
        // 如果 message 里包含 errors（来自校验），仍然统一返回
        ApiErrorResponse body = new ApiErrorResponse(
                ex.code(),
                ex.getMessage(),
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidBearer(InvalidBearerTokenException ex) {
        String msg = "无效或过期的 access_token";

        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            msg = ex.getMessage();
        } else if (ex.getCause() != null && ex.getCause().getMessage() != null && !ex.getCause().getMessage().isBlank()) {
            msg = ex.getCause().getMessage();
        }

        ApiErrorResponse body = new ApiErrorResponse(
                "AUTH_TOKEN_INVALID",
                msg,
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleDenied(AccessDeniedException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                "AUTH_FORBIDDEN",
                "无权限访问",
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknown(Exception ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                "INTERNAL_ERROR",
                "服务器内部错误",
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * 预留：如果你后续选择“校验失败不抛异常，而是返回 ValidationResult”，
     * 可以用这个 helper 把 ValidationError 映射成 ApiErrorResponse.FieldError。
     */
    public static List<ApiErrorResponse.FieldError> toFieldErrors(List<ValidationError> errors) {
        if (errors == null || errors.isEmpty()) return List.of();

        List<ApiErrorResponse.FieldError> out = new ArrayList<>();
        for (ValidationError e : errors) {
            out.add(new ApiErrorResponse.FieldError(e.field(), e.code(), e.message()));
        }
        return out;
    }
}
