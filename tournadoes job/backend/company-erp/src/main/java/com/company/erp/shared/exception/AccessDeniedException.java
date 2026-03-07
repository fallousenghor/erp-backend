package com.company.erp.shared.exception;

/**
 * Thrown when the authenticated user lacks permission for an operation.
 */
public class AccessDeniedException extends BusinessException {

    public AccessDeniedException() {
        super(ErrorCode.ACCESS_DENIED);
    }

    public AccessDeniedException(String detail) {
        super(ErrorCode.ACCESS_DENIED, detail);
    }
}
