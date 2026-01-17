package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.application.service.SellerOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;

    public SellerOrderController(SellerOrderService sellerOrderService) {
        this.sellerOrderService = sellerOrderService;
    }

    @GetMapping
    public ResponseEntity<List<SellerOrderDTOResponse>> getOrders(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(sellerOrderService.getIncomingOrders(jwt.getSubject()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<SellerOrderDTOResponse> getOrderDetails(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(sellerOrderService.getOrderDetails(orderId, jwt.getSubject()));
    }

    // 发货
    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<?> shipOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.shipOrder(orderId, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    // [新增] 标记送达
    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<?> deliverOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.deliverOrder(orderId, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    // 审核取消
    @PostMapping("/{orderId}/audit-cancel")
    public ResponseEntity<?> auditCancel(
            @PathVariable Long orderId,
            @RequestBody Map<String, Boolean> body,
            @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.auditCancellation(orderId, jwt.getSubject(), body.get("approved"));
        return ResponseEntity.ok(Map.of("message", "Audit processed"));
    }

    // 强制取消
    @PostMapping("/{orderId}/force-cancel")
    public ResponseEntity<?> forceCancel(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.forceCancel(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Order force cancelled"));
    }
}