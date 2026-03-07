package com.company.erp.modules.education.application.dto.request;

import com.company.erp.modules.education.domain.model.valueobject.ProgramLevel;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateProgramRequest(
        @NotBlank String title,
        String description,
        @NotNull ProgramLevel level,
        @Min(1) int durationWeeks,
        @Min(1) Integer maxStudents,
        LocalDate startDate,
        LocalDate endDate,
        @DecimalMin("0") @DecimalMax("20") BigDecimal passingScore
) {}
