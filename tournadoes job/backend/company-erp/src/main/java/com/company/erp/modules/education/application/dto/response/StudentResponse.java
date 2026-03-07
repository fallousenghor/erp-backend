package com.company.erp.modules.education.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record StudentResponse(
        UUID id,
        String studentCode,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate birthDate,
        boolean active,
        UUID employeeId,
        LocalDateTime createdAt
) {}
