package com.backend.flowershop.domain.model;

import java.util.List;

public record TokenClaims(
        String subject,
        String username,
        String email,
        List<String> groups
) {
}
