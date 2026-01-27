package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.CartItemDTORequest;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.domain.model.CartItem;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.CartRepository;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final FlowerRepository flowerRepository; // ✅ 新增依赖

    // 注入 S3 域名，用于拼接完整的图片链接
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    // ✅ 更新构造函数注入
    public CartService(CartRepository cartRepository, FlowerRepository flowerRepository) {
        this.cartRepository = cartRepository;
        this.flowerRepository = flowerRepository;
    }

    // 1. 添加到购物车
    @Transactional
    public void addToCart(String userId, CartItemDTORequest request) {
        // 简单的校验
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }

        // ✅ 核心防刷单逻辑
        Flower flower = flowerRepository.findById(request.flowerId()); // 假设 Repository 有 findById，稍后确认
        if (flower == null) {
            throw new RuntimeException("Flower not found");
        }

        // 🛡️ 禁止卖家购买自己的商品
        if (flower.getSellerId().equals(userId)) {
            throw new RuntimeException("Self-purchasing is prohibited (Anti-wash trading rule).");
        }

        // 🛡️ 检查库存
        if (flower.getStock() < request.quantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        cartRepository.addToCart(userId, request.flowerId(), request.quantity());
    }

    // 2. 获取我的购物车 (Entity -> DTO 转换)
    public List<CartItemDTOResponse> getMyCart(String userId) {
        List<CartItem> items = cartRepository.findAllByUserId(userId);

        return items.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 3. 移除商品
    public void removeFromCart(String userId, Long cartId) {
        cartRepository.deleteCartItem(cartId, userId);
    }

    public void updateCartItemQuantity(String userId, Long cartId, int quantity) {
        if (quantity <= 0) {
            // 如果数量减到0，直接移除商品
            cartRepository.deleteCartItem(cartId, userId);
        } else {
            cartRepository.updateQuantity(cartId, userId, quantity);
        }
    }

    // --- Mapper Helper ---
    private CartItemDTOResponse mapToDTO(CartItem item) {
        // 拼接图片完整 URL
        String fullUrl = item.getFlowerImageUrl();
        if (fullUrl != null && !fullUrl.startsWith("http")) {
            fullUrl = s3BaseUrl + fullUrl;
        }

        // 计算小计
        double subtotal = item.getFlowerPrice() * item.getQuantity();

        return new CartItemDTOResponse(
                item.getId(),
                item.getFlowerId(),
                item.getFlowerName(),
                item.getFlowerPrice(),
                fullUrl,
                item.getQuantity(),
                subtotal
        );
    }
}