package com.company.erp.modules.inventory.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AssetAssignmentResponse(
        UUID id,
        UUID assetId,
        String assetCode,
        String assetName,
        UUID employeeId,
        String employeeName,
        LocalDate assignedDate,
        LocalDate returnedDate,
        boolean active,
        String notes,
        LocalDateTime createdAt
) {}
