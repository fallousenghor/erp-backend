package com.company.erp.shared.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Centralized error codes for the entire application.
 * Each code carries an HTTP status and a human-readable message.
 */
@Getter
public enum ErrorCode {

    // ── Generic ────────────────────────────────────────────────────────────
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authentication required"),
    CONFLICT(HttpStatus.CONFLICT, "Resource already exists"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad request"),

    // ── Auth ───────────────────────────────────────────────────────────────
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid username or password"),
    ACCOUNT_LOCKED(HttpStatus.LOCKED, "Account is locked due to too many failed attempts"),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "Account is disabled"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Token has expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "Token is invalid"),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh token not found"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    ROLE_NOT_FOUND(HttpStatus.NOT_FOUND, "Role not found"),

    // ── Organization ───────────────────────────────────────────────────────
    DEPARTMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Department not found"),
    DEPARTMENT_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Department code already exists"),
    POSITION_NOT_FOUND(HttpStatus.NOT_FOUND, "Position not found"),

    // ── HR ─────────────────────────────────────────────────────────────────
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND, "Employee not found"),
    EMPLOYEE_ALREADY_TERMINATED(HttpStatus.CONFLICT, "Employee is already terminated"),
    LEAVE_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Leave request not found"),
    LEAVE_REQUEST_ALREADY_PROCESSED(HttpStatus.CONFLICT, "Leave request has already been processed"),
    INSUFFICIENT_LEAVE_BALANCE(HttpStatus.BAD_REQUEST, "Insufficient leave balance"),
    ATTENDANCE_ALREADY_RECORDED(HttpStatus.CONFLICT, "Attendance already recorded for this date"),

    // ── Finance ────────────────────────────────────────────────────────────
    INVOICE_NOT_FOUND(HttpStatus.NOT_FOUND, "Invoice not found"),
    INVOICE_ALREADY_PAID(HttpStatus.CONFLICT, "Invoice is already paid"),
    INVOICE_ALREADY_CANCELLED(HttpStatus.CONFLICT, "Invoice is already cancelled"),
    INVOICE_INVALID_TRANSITION(HttpStatus.BAD_REQUEST, "Invalid invoice status transition"),
    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Payment not found"),
    EXPENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "Expense not found"),

    // ── Inventory ──────────────────────────────────────────────────────────
    ASSET_NOT_FOUND(HttpStatus.NOT_FOUND, "Asset not found"),
    ASSET_ALREADY_ASSIGNED(HttpStatus.CONFLICT, "Asset is already assigned"),
    ASSET_NOT_ASSIGNED(HttpStatus.BAD_REQUEST, "Asset is not currently assigned"),
    ASSET_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Asset code already exists"),

    // ── Education ──────────────────────────────────────────────────────────
    PROGRAM_NOT_FOUND(HttpStatus.NOT_FOUND, "Training program not found"),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Student not found"),
    STUDENT_CODE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Student code already exists"),
    TEACHER_NOT_FOUND(HttpStatus.NOT_FOUND, "Teacher not found"),
    ENROLLMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Enrollment not found"),
    STUDENT_ALREADY_ENROLLED(HttpStatus.CONFLICT, "Student is already enrolled in this program"),
    ENROLLMENT_ALREADY_COMPLETED(HttpStatus.CONFLICT, "Enrollment is already completed");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
