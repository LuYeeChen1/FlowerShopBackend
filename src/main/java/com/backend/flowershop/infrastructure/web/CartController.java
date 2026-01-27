package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.CartItemDTORequest;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.application.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt; // ✅ 注意这个新 Import
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. 添加商品: POST /api/cart
    @PostMapping
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal Jwt jwt, // ✅ 修复：类型改为 Jwt
            @RequestBody CartItemDTORequest request) {

        // 🛡️ 防御性检查
        if (jwt == null) {
            log.warn("Unauthorized attempt to add to cart (Jwt is null)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User must be logged in"));
        }

        // ✅ 从 JWT 中直接获取 Subject (即 Cognito User ID / sub)
        String userId = jwt.getSubject();
        log.info("Adding item to cart for user: {}", userId);

        cartService.addToCart(userId, request);

        return ResponseEntity.ok(Map.of("message", "Item added to cart"));
    }

    // 2. 查看购物车: GET /api/cart
    @GetMapping
    public ResponseEntity<?> getMyCart(
            @AuthenticationPrincipal Jwt jwt) { // ✅ 修复：类型改为 Jwt

        if (jwt == null) {
            log.warn("Unauthorized attempt to view cart");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwt.getSubject();
        List<CartItemDTOResponse> cart = cartService.getMyCart(userId);
        return ResponseEntity.ok(cart);
    }

    // 3. 移除商品: DELETE /api/cart/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(
            @AuthenticationPrincipal Jwt jwt, // ✅ 修复：类型改为 Jwt
            @PathVariable Long id) {

        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userId = jwt.getSubject();
        cartService.removeFromCart(userId, id);

        return ResponseEntity.ok(Map.of("message", "Item removed"));
    }

    // 更新数量接口: PATCH /api/cart/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateQuantity(
            @AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt,
            @PathVariable Long id,
            @RequestBody Map<String, Integer> body) {

        if (jwt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String userId = jwt.getSubject();
        Integer quantity = body.get("quantity");

        cartService.updateCartItemQuantity(userId, id, quantity);
        return ResponseEntity.ok(Map.of("message", "Quantity updated"));
    }
}