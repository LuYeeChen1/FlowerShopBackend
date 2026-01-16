package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.service.OrderService;
import com.backend.flowershop.domain.model.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateOrderRequestDTO request) {

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userId = jwt.getSubject();
            // ✅ 获取登录账号的 Email (Cognito Token 中通常包含 "email" 字段)
            String userEmail = jwt.getClaimAsString("email");

            // 如果 Token 里没 email，就 fallback 到 userId (或者抛错)
            if (userEmail == null) userEmail = userId;

            // 将 email 传给 Service
            Long orderId = orderService.checkout(userId, userEmail, request);

            return ResponseEntity.ok(Map.of(
                    "message", "Order placed successfully",
                    "orderId", orderId
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(orderService.getUserOrders(jwt.getSubject()));
    }
}