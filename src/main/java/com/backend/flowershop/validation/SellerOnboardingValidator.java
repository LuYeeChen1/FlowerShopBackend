package com.backend.flowershop.validation;

import com.backend.flowershop.application.port.usecase.SellerOnboardingCommand;
import com.backend.flowershop.validation.normalize.SellerOnboardingNormalizer;
import com.backend.flowershop.validation.rules.SellerOnboardingRule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SellerOnboardingValidator {
    private final List<SellerOnboardingRule> rules;
    private final SellerOnboardingNormalizer normalizer;

    public SellerOnboardingValidator(
            List<SellerOnboardingRule> rules,
            SellerOnboardingNormalizer normalizer
    ) {
        this.rules = rules;
        this.normalizer = normalizer;
    }

    public SellerOnboardingValidation validate(SellerOnboardingCommand command) {
        SellerOnboardingCommand normalizedCommand = normalizer.normalize(command);
        ValidationResult result = new ValidationResult();
        for (SellerOnboardingRule rule : rules) {
            ValidationResult ruleResult = rule.validate(normalizedCommand);
            ruleResult.errors().forEach(error -> result.addError(error.code(), error.message()));
        }
        return new SellerOnboardingValidation(normalizedCommand, result);
    }
}
