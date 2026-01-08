package com.backend.flowershop.interfaces.controller;

import com.backend.flowershop.application.port.usecase.CurrentUserProfileUseCase;
import com.backend.flowershop.domain.model.TokenClaims;
import com.backend.flowershop.domain.model.UserProfile;
import com.backend.flowershop.interfaces.controller.dto.UserProfileResponse;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/me")
public class MeController {
    private final CurrentUserProfileUseCase currentUserProfileUseCase;

    public MeController(CurrentUserProfileUseCase currentUserProfileUseCase) {
        this.currentUserProfileUseCase = currentUserProfileUseCase;
    }

    @GetMapping
    public UserProfileResponse me(@AuthenticationPrincipal Jwt jwt) {
        TokenClaims claims = new TokenClaims(
                jwt.getSubject(),
                resolveUsername(jwt),
                jwt.getClaimAsString("email"),
                resolveGroups(jwt)
        );
        UserProfile profile = currentUserProfileUseCase.fetch(claims);
        return new UserProfileResponse(
                profile.subject(),
                profile.username(),
                profile.email(),
                profile.groups()
        );
    }

    private String resolveUsername(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsString("cognito:username"))
                .orElseGet(() -> jwt.getClaimAsString("username"));
    }

    private List<String> resolveGroups(Jwt jwt) {
        List<String> groups = jwt.getClaimAsStringList("cognito:groups");
        return groups == null ? List.of() : groups;
    }
}
