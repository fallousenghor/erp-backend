package com.company.erp.modules.auth.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

/**
 * Password Value Object — validates raw password before hashing.
 * Min 8 chars, at least 1 digit, 1 uppercase, 1 lowercase, 1 special char.
 */
public record Password(String value) {

    public Password {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Password cannot be blank");
        }
        if (value.length() < 8) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must be at least 8 characters long");
        }
        if (!value.matches(".*[A-Z].*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one uppercase letter");
        }
        if (!value.matches(".*[a-z].*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one lowercase letter");
        }
        if (!value.matches(".*\\d.*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one digit");
        }
        if (!value.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Password must contain at least one special character");
        }
    }

    @Override
    public String toString() {
        return "***";
    }
}
