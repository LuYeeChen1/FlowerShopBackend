package com.backend.flowershop.domain.model;

import java.time.OffsetDateTime;

public record SellerOnboarding(
        SellerOnboardingId id,
        String storeName,
        SellerContact contact,
        SellerOnboardingStatus status,
        OffsetDateTime createdAt
) {
}
