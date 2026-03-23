package com.company.erp.shared.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Common errors
    EMPLOYEE_NOT_FOUND("EMPLOYEE_NOT_FOUND", "Employee not found", HttpStatus.NOT_FOUND),
    LEAVE_NOT_FOUND("LEAVE_NOT_FOUND", "Leave request not found", HttpStatus.NOT_FOUND),
    ATTENDANCE_NOT_FOUND("ATTENDANCE_NOT_FOUND", "Attendance record not found", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("VALIDATION_ERROR", "Validation error", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("BAD_REQUEST", "Bad request", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED("ACCESS_DENIED", "Access denied", HttpStatus.FORBIDDEN),
    CONFLICT("CONFLICT", "Resource conflict", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND),
    
    // Authentication & Authorization
    INVALID_CREDENTIALS("INVALID_CREDENTIALS", "Invalid username or password", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("ACCOUNT_LOCKED", "Account is locked", HttpStatus.LOCKED),
    USER_NOT_FOUND("USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND),
    
    // HR Module
    DEPARTMENT_NOT_FOUND("DEPARTMENT_NOT_FOUND", "Department not found", HttpStatus.NOT_FOUND),
    DEPARTMENT_CODE_ALREADY_EXISTS("DEPARTMENT_CODE_ALREADY_EXISTS", "Department code already exists", HttpStatus.CONFLICT),
    POSITION_NOT_FOUND("POSITION_NOT_FOUND", "Position not found", HttpStatus.NOT_FOUND),
    
    // Education Module
    TEACHER_NOT_FOUND("TEACHER_NOT_FOUND", "Teacher not found", HttpStatus.NOT_FOUND),
    STUDENT_NOT_FOUND("STUDENT_NOT_FOUND", "Student not found", HttpStatus.NOT_FOUND),
    STUDENT_ALREADY_ENROLLED("STUDENT_ALREADY_ENROLLED", "Student already enrolled", HttpStatus.CONFLICT),
    
    // Finance Module
    PAYMENT_NOT_FOUND("PAYMENT_NOT_FOUND", "Payment not found", HttpStatus.NOT_FOUND),
    INVOICE_NOT_FOUND("INVOICE_NOT_FOUND", "Invoice not found", HttpStatus.NOT_FOUND),
    INVOICE_INVALID_TRANSITION("INVOICE_INVALID_TRANSITION", "Invalid invoice status transition", HttpStatus.BAD_REQUEST),
    EXPENSE_NOT_FOUND("EXPENSE_NOT_FOUND", "Expense not found", HttpStatus.NOT_FOUND),
    
    // Inventory Module
    ASSET_ALREADY_ASSIGNED("ASSET_ALREADY_ASSIGNED", "Asset already assigned", HttpStatus.CONFLICT),
    ASSET_NOT_FOUND("ASSET_NOT_FOUND", "Asset not found", HttpStatus.NOT_FOUND),
    ASSET_NOT_ASSIGNED("ASSET_NOT_ASSIGNED", "Asset not assigned", HttpStatus.BAD_REQUEST),
    
    // Education Module
    PROGRAM_NOT_FOUND("PROGRAM_NOT_FOUND", "Training program not found", HttpStatus.NOT_FOUND),
    ENROLLMENT_ALREADY_COMPLETED("ENROLLMENT_ALREADY_COMPLETED", "Enrollment already completed", HttpStatus.CONFLICT),
    
    // HR Module
    EMPLOYEE_ALREADY_TERMINATED("EMPLOYEE_ALREADY_TERMINATED", "Employee already terminated", HttpStatus.CONFLICT),
    INSUFFICIENT_LEAVE_BALANCE("INSUFFICIENT_LEAVE_BALANCE", "Insufficient leave balance", HttpStatus.BAD_REQUEST),
    
    // Auth Module
    USER_ALREADY_EXISTS("USER_ALREADY_EXISTS", "User already exists", HttpStatus.CONFLICT),
    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "Role not found", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_NOT_FOUND("REFRESH_TOKEN_NOT_FOUND", "Refresh token not found", HttpStatus.NOT_FOUND),
    REFRESH_TOKEN_EXPIRED("REFRESH_TOKEN_EXPIRED", "Refresh token expired", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("TOKEN_INVALID", "Invalid token", HttpStatus.UNAUTHORIZED),
    
    // Education Module
    ENROLLMENT_NOT_FOUND("ENROLLMENT_NOT_FOUND", "Enrollment not found", HttpStatus.NOT_FOUND);
    
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    
    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
