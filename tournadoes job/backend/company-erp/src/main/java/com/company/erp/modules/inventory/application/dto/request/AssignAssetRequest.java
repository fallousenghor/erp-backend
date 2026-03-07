package com.company.erp.modules.inventory.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AssignAssetRequest(
        @NotNull UUID employeeId,
        @NotNull String employeeName,
        @NotNull LocalDate assignedDate,
        String notes
) {}
