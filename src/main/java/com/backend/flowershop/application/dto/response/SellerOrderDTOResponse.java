package com.backend.flowershop.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SellerOrderDTOResponse(
        Long orderId,
        String buyerName,       // 填写的收货人姓名
        String buyerPhone,      // 填写的电话
        String buyerEmail,      // ✅ 从账号 Token 获取的邮箱
        String shippingAddress, // 纯地址
        BigDecimal totalPrice,
        String status,
        LocalDateTime createdAt,
        List<SellerOrderItemDTO> items
) {
    public record SellerOrderItemDTO(
            String flowerName,
            Integer quantity,
            BigDecimal price,
            String imageUrl
    ) {}
}