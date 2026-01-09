package com.backend.flowershop.validation.ruleimpl;

import com.backend.flowershop.validation.ValidationResult;
import com.backend.flowershop.validation.rules.Rules;
import org.springframework.security.oauth2.jwt.Jwt;

public class AccessTokenRule implements Rules<Jwt> {
    private final String clientId;

    public AccessTokenRule(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public ValidationResult validate(Jwt token) {
        ValidationResult result = new ValidationResult();
        String tokenUse = token.getClaimAsString("token_use");
        if (!"access".equals(tokenUse)) {
            result.addError("invalid_token", "Token use must be access");
            return result;
        }
        String tokenClientId = token.getClaimAsString("client_id");
        if (!clientId.equals(tokenClientId)) {
            result.addError("invalid_token", "Invalid access token client_id");
        }
        return result;
    }
}
