package com.company.erp.modules.auth.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

import java.util.regex.Pattern;

/**
 * Email Value Object — immutable, validated on creation.
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Email cannot be blank");
        }
        if (!EMAIL_PATTERN.matcher(value.trim()).matches()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Invalid email format: " + value);
        }
        value = value.trim().toLowerCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
