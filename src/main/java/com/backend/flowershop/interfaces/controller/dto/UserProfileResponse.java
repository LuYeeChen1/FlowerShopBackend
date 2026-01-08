package com.backend.flowershop.interfaces.controller.dto;

import java.util.List;

public record UserProfileResponse(
        String subject,
        String username,
        String email,
        List<String> groups
) {
}
