package com.backend.flowershop.validation.ruleimpl;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.validation.ValidationResult;
import com.backend.flowershop.validation.rules.SellerOnboardingRule;
import java.util.regex.Pattern;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(120)
public class SO030 implements SellerOnboardingRule {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9+()\\-\\s]{7,20}$");

    @Override
    public ValidationResult validate(SellerOnboardingCommand input) {
        ValidationResult result = new ValidationResult();
        String phone = input.contactPhone();
        if (phone != null && !PHONE_PATTERN.matcher(phone).matches()) {
            result.addError("invalid_phone", "Contact phone format is invalid.");
        }
        return result;
    }
}
