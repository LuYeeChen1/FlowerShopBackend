package com.backend.flowershop.infrastructure.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

public class AccessTokenValidator implements OAuth2TokenValidator<Jwt> {
    private final String clientId;

    public AccessTokenValidator(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        String tokenUse = token.getClaimAsString("token_use");
        if (!"access".equals(tokenUse)) {
            return invalidToken("Token use must be access");
        }
        String tokenClientId = token.getClaimAsString("client_id");
        if (!clientId.equals(tokenClientId)) {
            return invalidToken("Invalid access token client_id");
        }
        return OAuth2TokenValidatorResult.success();
    }

    private OAuth2TokenValidatorResult invalidToken(String message) {
        OAuth2Error error = new OAuth2Error("invalid_token", message, null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
