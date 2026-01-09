package com.backend.flowershop.application.port.persistence;

import com.backend.flowershop.domain.model.SellerOnboarding;

public interface SellerOnboardingRepository {
    void save(SellerOnboarding onboarding);
}
