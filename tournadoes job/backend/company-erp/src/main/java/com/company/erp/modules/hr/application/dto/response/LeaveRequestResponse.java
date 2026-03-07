package com.company.erp.modules.hr.application.dto.response;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.domain.model.valueobject.LeaveType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LeaveRequestResponse(
        UUID id,
        UUID employeeId,
        String employeeName,
        LeaveType leaveType,
        LocalDate startDate,
        LocalDate endDate,
        int daysRequested,
        String reason,
        LeaveRequest.LeaveStatus status,
        String approvedBy,
        String rejectionReason,
        LocalDateTime createdAt
) {}
