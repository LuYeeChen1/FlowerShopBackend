package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.response.UserDTOResponse; // Updated Import
import com.backend.flowershop.application.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDTOResponse syncAndUserProfile(@AuthenticationPrincipal Jwt jwt) {
        return userService.syncCognitoUser(
                jwt.getSubject(),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("username"),
                jwt.getClaimAsStringList("cognito:groups")
        );
    }
}