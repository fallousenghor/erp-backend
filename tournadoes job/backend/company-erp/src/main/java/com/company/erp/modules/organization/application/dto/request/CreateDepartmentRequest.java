package com.company.erp.modules.organization.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record CreateDepartmentRequest(
        @NotBlank(message = "Department name is required")
        @Size(min = 2, max = 100)
        String name,

        @NotBlank(message = "Department code is required")
        @Pattern(regexp = "^[A-Z0-9_]{2,10}$",
                message = "Code must be 2-10 uppercase letters, digits or underscores")
        String code,

        @Size(max = 500)
        String description,

        @jakarta.validation.constraints.DecimalMin(value = "0.0", inclusive = false, message = "Budget must be positive")
        java.math.BigDecimal budget
) {}
