package com.backend.flowershop.application.dto.response;

import java.math.BigDecimal;

// 用于展示层显示的鲜花信息
public record FlowerDTOResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        String category
) {}