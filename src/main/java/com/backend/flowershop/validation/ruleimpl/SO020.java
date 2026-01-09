package com.backend.flowershop.validation.ruleimpl;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.validation.ValidationResult;
import com.backend.flowershop.validation.rules.SellerOnboardingRule;
import java.util.regex.Pattern;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(110)
public class SO020 implements SellerOnboardingRule {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    @Override
    public ValidationResult validate(SellerOnboardingCommand input) {
        ValidationResult result = new ValidationResult();
        String email = normalize(input.contactEmail());
        if (email != null && !EMAIL_PATTERN.matcher(email).matches()) {
            result.addError("invalid_email", "Contact email format is invalid.");
        }
        return result;
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isBlank() ? null : trimmed;
    }
}
