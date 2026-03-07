package com.company.erp.modules.hr.application.dto.request;

import com.company.erp.modules.hr.domain.model.valueobject.LeaveType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LeaveRequestDto(
        @NotNull LeaveType leaveType,
        @NotNull(message = "Start date is required") LocalDate startDate,
        @NotNull(message = "End date is required") LocalDate endDate,
        String reason
) {}
