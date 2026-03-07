package com.company.erp.modules.education.application.dto.response;

import com.company.erp.modules.education.domain.model.valueobject.ProgramLevel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TrainingProgramResponse(
        UUID id,
        String title,
        String description,
        ProgramLevel level,
        int durationWeeks,
        int totalHours,
        Integer maxStudents,
        long currentEnrollments,
        LocalDate startDate,
        LocalDate endDate,
        boolean active,
        BigDecimal passingScore,
        List<ModuleResponse> modules,
        LocalDateTime createdAt
) {
    public record ModuleResponse(
            UUID id,
            String title,
            int durationHours,
            double coefficient,
            int orderIndex,
            String teacherName
    ) {}
}
