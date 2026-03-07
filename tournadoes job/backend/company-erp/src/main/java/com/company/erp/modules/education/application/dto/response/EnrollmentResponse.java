package com.company.erp.modules.education.application.dto.response;

import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record EnrollmentResponse(
        UUID id,
        UUID studentId,
        String studentName,
        String studentCode,
        UUID programId,
        String programTitle,
        LocalDate enrollmentDate,
        LocalDate completionDate,
        EnrollmentStatus status,
        BigDecimal finalAverage,
        String finalLetterGrade,
        boolean passed,
        List<GradeResponse> grades,
        LocalDateTime createdAt
) {
    public record GradeResponse(
            UUID moduleId,
            String moduleTitle,
            BigDecimal score,
            BigDecimal maxScore,
            String letterGrade,
            boolean passed
    ) {}
}
