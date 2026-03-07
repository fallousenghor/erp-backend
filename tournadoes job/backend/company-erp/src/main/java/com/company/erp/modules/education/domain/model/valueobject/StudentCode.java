package com.company.erp.modules.education.domain.model.valueobject;

import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;

/**
 * StudentCode Value Object — format: STU-YYYY-NNNNNN
 */
public record StudentCode(String value) {

    public StudentCode {
        if (value == null || value.isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Student code cannot be blank");
        }
        value = value.trim().toUpperCase();
    }

    public static StudentCode generate() {
        String year = String.valueOf(java.time.LocalDate.now().getYear());
        String seq  = String.format("%06d", (int)(Math.random() * 999999));
        return new StudentCode("STU-" + year + "-" + seq);
    }

    @Override
    public String toString() { return value; }
}
