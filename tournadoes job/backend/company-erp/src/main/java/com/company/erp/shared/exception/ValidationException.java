package com.company.erp.shared.exception;

import lombok.Getter;

import java.util.Map;

/**
 * Thrown when business-level validation fails.
 * Can carry a map of field → error for detailed reporting.
 */
@Getter
public class ValidationException extends BusinessException {

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.errors = Map.of();
    }

    public ValidationException(Map<String, String> errors) {
        super(ErrorCode.VALIDATION_ERROR);
        this.errors = errors;
    }
}
