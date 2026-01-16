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

    @PatchMapping("/{id}/ship")
    public ResponseEntity<?> shipOrder(@PathVariable Long id) {
        sellerOrderService.shipOrder(id);
        return ResponseEntity.ok(Map.of("message", "Order marked as SHIPPED"));
    }
}