package com.company.erp.modules.auth.application.dto.response;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        boolean enabled,
        boolean locked,
        Set<String> roles,
        LocalDateTime lastLogin,
        LocalDateTime createdAt
) {}
