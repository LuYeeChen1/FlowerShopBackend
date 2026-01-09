package com.backend.flowershop.validation;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.validation.rules.SellerOnboardingRule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerOnboardingValidator {
    private final List<SellerOnboardingRule> rules;

    public SellerOnboardingValidator(List<SellerOnboardingRule> rules) {
        this.rules = rules;
    }

    public ValidationResult validate(SellerOnboardingCommand command) {
        ValidationResult result = new ValidationResult();
        for (SellerOnboardingRule rule : rules) {
            ValidationResult ruleResult = rule.validate(command);
            ruleResult.errors().forEach(error -> result.addError(error.code(), error.message()));
        }
        return result;
    }
}
