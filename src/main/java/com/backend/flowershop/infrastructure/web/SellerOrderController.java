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

    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<?> shipOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.shipOrder(orderId, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{orderId}/deliver")
    public ResponseEntity<?> deliverOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.deliverOrder(orderId, jwt.getSubject());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{orderId}/audit-cancel")
    public ResponseEntity<?> auditCancel(
            @PathVariable Long orderId,
            @RequestBody Map<String, Boolean> body,
            @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.auditCancellation(orderId, jwt.getSubject(), body.get("approved"));
        return ResponseEntity.ok(Map.of("message", "Audit processed"));
    }

    @PostMapping("/{orderId}/force-cancel")
    public ResponseEntity<?> forceCancel(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.forceCancel(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Order force cancelled"));
    }

    // [新增] 卖家删除历史记录
    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.deleteOrderFromHistory(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Order removed from history"));
    }
}