package com.backend.flowershop.application.port.integration;

import com.backend.flowershop.domain.model.SellerOnboarding;

public interface SellerOnboardingLambdaPort {
    void invoke(SellerOnboarding onboarding);
}
