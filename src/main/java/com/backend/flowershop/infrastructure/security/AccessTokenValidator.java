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
        String tokenClientId = token.getClaimAsString("client_id");
        if ("access".equals(tokenUse) && clientId.equals(tokenClientId)) {
            return OAuth2TokenValidatorResult.success();
        }
        OAuth2Error error = new OAuth2Error("invalid_token", "Invalid access token", null);
        return OAuth2TokenValidatorResult.failure(error);
    }
}
