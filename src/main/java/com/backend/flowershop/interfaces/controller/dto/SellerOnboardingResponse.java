package com.backend.flowershop.interfaces.controller.dto;

public record SellerOnboardingResponse(
        String id,
        String status,
        String storeName,
        String contactName,
        String contactEmail,
        String contactPhone
) {
}
