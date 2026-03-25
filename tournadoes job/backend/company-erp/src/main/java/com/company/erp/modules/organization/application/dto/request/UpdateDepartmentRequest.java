package com.company.erp.modules.organization.application.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Optional;

@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public record UpdateDepartmentRequest(
        @Size(min = 2, max = 100)
        String name,
        
        @Size(max = 500)
        String description,
        
        Boolean active,
        
        @DecimalMin(value = "0.0", message = "Budget must be non-negative")
        BigDecimal budget
) {
    public Optional<BigDecimal> getBudget() {
        return Optional.ofNullable(budget);
    }
}

