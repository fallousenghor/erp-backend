package com.company.erp.modules.organization.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

/**
 * DepartmentName Value Object — immutable, validated.
 */
public record DepartmentName(String value) {

    public DepartmentName {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Department name cannot be blank");
        }
        if (value.length() < 2 || value.length() > 100) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Department name must be between 2 and 100 characters");
        }
        value = value.trim();
    }

    @Override
    public String toString() {
        return value;
    }
}
