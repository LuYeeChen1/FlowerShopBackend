package com.backend.flowershop.application.port.usecase;

import com.backend.flowershop.domain.model.TokenClaims;
import com.backend.flowershop.domain.model.UserProfile;

public interface CurrentUserProfileUseCase {
    UserProfile fetch(TokenClaims claims);
}
