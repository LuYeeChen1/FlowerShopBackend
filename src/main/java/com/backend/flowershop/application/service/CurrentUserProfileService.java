package com.backend.flowershop.application.service;

import com.backend.flowershop.application.port.usecase.CurrentUserProfileUseCase;
import com.backend.flowershop.domain.model.TokenClaims;
import com.backend.flowershop.domain.model.UserProfile;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserProfileService implements CurrentUserProfileUseCase {

    @Override
    public UserProfile fetch(TokenClaims claims) {
        List<String> groups = claims.groups() == null ? List.of() : claims.groups();
        return new UserProfile(
                claims.subject(),
                claims.username(),
                claims.email(),
                groups
        );
    }
}
