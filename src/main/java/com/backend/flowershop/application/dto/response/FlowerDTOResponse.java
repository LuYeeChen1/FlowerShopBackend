package com.backend.flowershop.application.dto.response;

import java.math.BigDecimal;

/**
 * [Layer 1] 列表概览 DTO
 * 用于 Dashboard 列表或首页瀑布流，不包含卖家详细档案或敏感库存日志。
 */
public record FlowerDTOResponse(
        String id,          // 前端通常喜欢 String 类型的 ID
        String name,
        String description, // 列表页可能只展示简短描述
        BigDecimal price,
        String imageUrl,
        String category
) {}