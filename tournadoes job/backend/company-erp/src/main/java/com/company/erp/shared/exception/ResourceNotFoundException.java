package com.company.erp.shared.exception;

import java.util.UUID;

/**
 * Thrown when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ResourceNotFoundException(ErrorCode errorCode, UUID id) {
        super(errorCode, errorCode.getMessage() + " with id: " + id);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String identifier) {
        super(errorCode, errorCode.getMessage() + ": " + identifier);
    }
}
