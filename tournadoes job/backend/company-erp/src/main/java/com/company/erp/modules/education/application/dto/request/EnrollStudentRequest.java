package com.company.erp.modules.education.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record EnrollStudentRequest(
        @NotNull UUID studentId,
        @NotNull UUID programId,
        LocalDate enrollmentDate
) {}
