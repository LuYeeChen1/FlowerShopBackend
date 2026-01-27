package com.backend.flowershop.application.dto.response;

import java.math.BigDecimal;

public record FlowerDTOResponse(
        String id,
        String name,
        String description,
        BigDecimal price,
        Integer stock,
        String imageUrl,
        String category
) {}