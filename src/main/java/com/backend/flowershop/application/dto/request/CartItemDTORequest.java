package com.backend.flowershop.application.dto.request;

public record CartItemDTORequest(
        Long flowerId,
        Integer quantity
) {}