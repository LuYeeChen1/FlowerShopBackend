package com.backend.flowershop.validation;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;

public record SellerOnboardingValidation(
        SellerOnboardingCommand command,
        ValidationResult result
) {
}
