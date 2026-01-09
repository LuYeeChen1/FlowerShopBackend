package com.backend.flowershop.application.port.usecase;

public record SellerOnboardingCommand(
        String storeName,
        String contactName,
        String contactEmail,
        String contactPhone
) {
}
