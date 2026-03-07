package com.company.erp.modules.organization.application.dto.response;

import java.util.UUID;

public record PositionResponse(
        UUID id,
        String title,
        String description,
        Double minSalary,
        Double maxSalary,
        boolean active,
        UUID departmentId,
        String departmentName
) {}
