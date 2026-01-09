package com.backend.flowershop.interfaces.controller;

import com.backend.flowershop.domain.error.DomainErrorException;
import com.backend.flowershop.interfaces.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DomainErrorHandler {

    @ExceptionHandler(DomainErrorException.class)
    public ResponseEntity<ErrorResponse> handleDomainError(DomainErrorException ex) {
        ErrorResponse response = new ErrorResponse(ex.code(), ex.getMessage());
        HttpStatus status = "VALIDATION_ERROR".equals(ex.code()) ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(status).body(response);
    }
}
