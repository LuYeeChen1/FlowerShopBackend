package com.backend.flowershop.validation.normalize;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import org.springframework.stereotype.Component;

@Component
public class SellerOnboardingNormalizer {
    public SellerOnboardingCommand normalize(SellerOnboardingCommand command) {
        return new SellerOnboardingCommand(
                normalizeValue(command.storeName()),
                normalizeValue(command.contactName()),
                normalizeValue(command.contactEmail()),
                normalizeValue(command.contactPhone())
        );
    }

    private String normalizeValue(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}
