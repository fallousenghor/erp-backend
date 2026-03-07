package com.company.erp.modules.auth.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

/**
 * Username Value Object — immutable, 3-50 chars, alphanumeric + underscore.
 */
public record Username(String value) {

    public Username {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Username cannot be blank");
        }
        if (value.length() < 3 || value.length() > 50) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Username must be between 3 and 50 characters");
        }
        if (!value.matches("^[a-zA-Z0-9_]+$")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Username can only contain letters, digits, and underscores");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
