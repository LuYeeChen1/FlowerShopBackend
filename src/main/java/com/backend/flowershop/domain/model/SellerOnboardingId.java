package com.backend.flowershop.domain.model;

import java.util.UUID;

public record SellerOnboardingId(String value) {
    public static SellerOnboardingId newId() {
        return new SellerOnboardingId(UUID.randomUUID().toString());
    }
}
