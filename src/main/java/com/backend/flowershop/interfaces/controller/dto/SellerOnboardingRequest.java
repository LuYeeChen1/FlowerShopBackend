package com.backend.flowershop.interfaces.controller.dto;

public record SellerOnboardingRequest(
        String storeName,
        String contactName,
        String contactEmail,
        String contactPhone
) {
}
