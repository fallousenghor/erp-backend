package com.company.erp.modules.organization.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record DepartmentResponse(
        UUID id,
        String name,
        String code,
        String description,
        boolean active,
        String currentHeadName,
        BigDecimal budget,
        int positionCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
