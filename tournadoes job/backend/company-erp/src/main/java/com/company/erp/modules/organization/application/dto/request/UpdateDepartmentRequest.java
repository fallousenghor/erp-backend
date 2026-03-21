package com.company.erp.modules.organization.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Size;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public record UpdateDepartmentRequest(
        @Size(min = 2, max = 100)
        String name,
        
        @Size(max = 500)
        String description,
        
        Boolean active
) {}

