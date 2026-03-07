package com.company.erp.modules.education.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

public record RecordGradeRequest(
        @NotNull UUID moduleId,
        @NotNull @DecimalMin("0") BigDecimal score,
        @NotNull @DecimalMin("1") BigDecimal maxScore,
        String comments
) {}
