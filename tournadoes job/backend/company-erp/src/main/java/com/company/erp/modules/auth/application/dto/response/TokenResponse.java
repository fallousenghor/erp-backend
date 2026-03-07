package com.company.erp.modules.auth.application.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
