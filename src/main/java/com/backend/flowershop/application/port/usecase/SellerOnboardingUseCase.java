package com.backend.flowershop.application.port.usecase;

import com.backend.flowershop.domain.model.SellerOnboarding;

public interface SellerOnboardingUseCase {
    SellerOnboarding submit(SellerOnboardingCommand command);
}
