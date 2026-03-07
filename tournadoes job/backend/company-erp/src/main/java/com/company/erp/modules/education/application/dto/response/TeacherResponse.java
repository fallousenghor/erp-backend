package com.company.erp.modules.education.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record TeacherResponse(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phone,
        String specialization,
        boolean active,
        UUID employeeId,
        LocalDateTime createdAt
) {}
