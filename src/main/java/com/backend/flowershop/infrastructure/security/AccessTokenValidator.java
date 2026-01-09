package com.backend.flowershop.infrastructure.security;

import com.backend.flowershop.validation.ValidationResult;
import com.backend.flowershop.validation.ruleimpl.AccessTokenRule;
import java.util.List;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AccessTokenValidator implements OAuth2TokenValidator<Jwt> {
    private final AccessTokenRule rule;

    public AccessTokenValidator(String clientId) {
        this.rule = new AccessTokenRule(clientId);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        ValidationResult result = rule.validate(token);
        if (!result.isValid()) {
            List<String> messages = result.errors().stream()
                .map(error -> error.message())
                .toList();
            return invalidToken(String.join(", ", messages));
        }
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult invalidToken(String message) {
        OAuth2Error error = new OAuth2Error("invalid_token", message, null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
