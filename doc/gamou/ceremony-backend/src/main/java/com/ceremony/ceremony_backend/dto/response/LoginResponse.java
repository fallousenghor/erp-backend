package com.ceremony.ceremony_backend.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
    private Long userId;
    private String username;
    private String role;
    private Boolean isTemporaryPassword;
    private Boolean isPasswordChangeable;

    public LoginResponse(String token, String tokenType, Long userId, String username, String role, Boolean isTemporaryPassword, Boolean isPasswordChangeable) {
        this.token = token;
        this.tokenType = tokenType;
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.isTemporaryPassword = isTemporaryPassword;
        this.isPasswordChangeable = isPasswordChangeable;
    }
}