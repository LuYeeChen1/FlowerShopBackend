package com.backend.flowershop.application.dto.response;

public record CartItemDTOResponse(
        Long id,             // 购物车条目 ID (用于删除)
        Long flowerId,       // 鲜花 ID (用于跳转详情)
        String name,         // 鲜花名
        Double price,        // 单价
        String imageUrl,     // 图片
        Integer quantity,    // 数量
        Double subtotal      // 小计 (price * quantity)
) {}