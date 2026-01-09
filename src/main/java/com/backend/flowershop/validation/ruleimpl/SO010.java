package com.backend.flowershop.validation.ruleimpl;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.validation.ValidationResult;
import com.backend.flowershop.validation.rules.SellerOnboardingRule;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(100)
public class SO010 implements SellerOnboardingRule {
    @Override
    public ValidationResult validate(SellerOnboardingCommand input) {
        ValidationResult result = new ValidationResult();
        if (isBlank(input.storeName())) {
            result.addError("required_field", "Store name is required.");
        }
        if (isBlank(input.contactName())) {
            result.addError("required_field", "Contact name is required.");
        }
        if (isBlank(input.contactEmail())) {
            result.addError("required_field", "Contact email is required.");
        }
        if (isBlank(input.contactPhone())) {
            result.addError("required_field", "Contact phone is required.");
        }
        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isBlank();
    }
}
