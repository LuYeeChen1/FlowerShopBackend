package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.service.OrderService;
import com.backend.flowershop.domain.enums.OrderStatus; // ✅ 引入
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

        if (jwt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        try {
            String userId = jwt.getSubject();
            String userEmail = jwt.getClaimAsString("email");
            if (userEmail == null) userEmail = userId;

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

    // --- [新增] 状态更新接口 ---
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> payload) {

        try {
            String statusStr = payload.get("status");
            if (statusStr == null) throw new RuntimeException("Status is required");

            // ✅ 将前端字符串安全转换为枚举
            OrderStatus newStatus;
            try {
                newStatus = OrderStatus.valueOf(statusStr); // 必须匹配 Enum 定义 (SHIPPED, DELIVERED)
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid status value: " + statusStr);
            }

            orderService.updateOrderStatus(orderId, newStatus);

            return ResponseEntity.ok(Map.of("message", "Status updated to " + newStatus));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{orderId}/cancel-request")
    public ResponseEntity<?> requestCancel(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        orderService.requestCancel(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Cancellation requested successfully"));
    }

    // ✅ [新增] 删除单个订单历史
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        orderService.deleteOrder(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Order removed from history"));
    }

    // ✅ [新增] 清空历史
    @DeleteMapping("/history")
    public ResponseEntity<?> clearHistory(@AuthenticationPrincipal Jwt jwt) {
        orderService.clearOrderHistory(jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "History cleared"));
    }
}