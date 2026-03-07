package com.company.erp.modules.organization.application.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateDepartmentRequest(
        @Size(min = 2, max = 100)
        String name,

        @Size(max = 500)
        String description,

        Boolean active
) {}
