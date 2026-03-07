package com.company.erp.modules.organization.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

/**
 * DepartmentCode Value Object — uppercase alphanumeric, max 10 chars.
 * Example: "IT", "HR", "FIN", "OPS"
 */
public record DepartmentCode(String value) {

    public DepartmentCode {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Department code cannot be blank");
        }
        value = value.trim().toUpperCase();
        if (value.length() < 2 || value.length() > 10) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Department code must be between 2 and 10 characters");
        }
        if (!value.matches("^[A-Z0-9_]+$")) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Department code can only contain uppercase letters, digits, and underscores");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
