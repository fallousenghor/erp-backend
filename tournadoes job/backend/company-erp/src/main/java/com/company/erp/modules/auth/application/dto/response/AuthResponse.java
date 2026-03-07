package com.company.erp.modules.auth.application.dto.response;

import java.util.Set;
import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String username,
        String email,
        String fullName,
        Set<String> permissions,
        String accessToken,
        String refreshToken,
        long accessTokenExpiresIn
) {}
