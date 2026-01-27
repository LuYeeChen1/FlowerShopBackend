package com.backend.flowershop.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 拦截所有异常 (Exception.class)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // 1. 🔥 在后端控制台打印完整堆栈信息 (这是给您看的)
        System.err.println("========== 🛑 SYSTEM ERROR CAUGHT 🛑 ==========");
        System.err.println("Error Type: " + ex.getClass().getName());
        System.err.println("Message:    " + ex.getMessage());
        System.err.println("Location:   " + ex.getStackTrace()[0]); // 打印报错的第一行代码位置
        ex.printStackTrace(); // 打印完整堆栈
        System.err.println("===============================================");

        // 2. 构造给前端的友好提示 (包含调试信息)
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", System.currentTimeMillis());
        body.put("status", 500);
        body.put("error", "Internal Server Error");
        body.put("exception", ex.getClass().getSimpleName()); // 告诉你是空指针还是SQL错误
        body.put("message", ex.getMessage()); // 具体的错误消息

        // 🔍 极致定位：告诉前端是哪个文件的哪一行出错了
        if (ex.getStackTrace().length > 0) {
            StackTraceElement elem = ex.getStackTrace()[0];
            body.put("location", elem.getClassName() + ":" + elem.getLineNumber());
        }

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}