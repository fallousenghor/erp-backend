package com.company.erp.modules.hr.domain.model;

import com.company.erp.modules.hr.domain.model.valueobject.LeaveType;
import com.company.erp.shared.base.BaseAuditEntity;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * LeaveRequest Aggregate Root.
 */
@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest extends BaseAuditEntity {

    public enum LeaveStatus { PENDING, APPROVED, REJECTED, CANCELLED }

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "employee_name", nullable = false, length = 160)
    private String employeeName;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_type", nullable = false, length = 20)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "days_requested", nullable = false)
    private int daysRequested;

    @Column(name = "reason", length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

    // ── Domain behavior ─────────────────────────────────────────────────────

    public static LeaveRequest create(UUID employeeId, String employeeName,
                                       LeaveType leaveType, LocalDate startDate,
                                       LocalDate endDate, String reason) {
        if (endDate.isBefore(startDate)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "End date cannot be before start date");
        }
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return LeaveRequest.builder()
                .employeeId(employeeId)
                .employeeName(employeeName)
                .leaveType(leaveType)
                .startDate(startDate)
                .endDate(endDate)
                .daysRequested(days)
                .reason(reason)
                .status(LeaveStatus.PENDING)
                .build();
    }

    public void approve(String approvedBy) {
        ensurePending();
        this.status = LeaveStatus.APPROVED;
        this.approvedBy = approvedBy;
    }

    public void reject(String approvedBy, String reason) {
        ensurePending();
        this.status = LeaveStatus.REJECTED;
        this.approvedBy = approvedBy;
        this.rejectionReason = reason;
    }

    public void cancel() {
        if (this.status == LeaveStatus.APPROVED || this.status == LeaveStatus.REJECTED) {
            throw new BusinessException(ErrorCode.LEAVE_REQUEST_ALREADY_PROCESSED);
        }
        this.status = LeaveStatus.CANCELLED;
    }

    private void ensurePending() {
        if (this.status != LeaveStatus.PENDING) {
            throw new BusinessException(ErrorCode.LEAVE_REQUEST_ALREADY_PROCESSED);
        }
    }
}
