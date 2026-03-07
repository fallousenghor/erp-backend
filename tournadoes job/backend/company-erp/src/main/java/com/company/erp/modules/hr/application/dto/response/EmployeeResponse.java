package com.company.erp.modules.hr.application.dto.response;

import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import com.company.erp.modules.hr.domain.model.valueobject.EmployeeStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String employeeNumber,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate birthDate,
        LocalDate hireDate,
        LocalDate terminationDate,
        EmployeeStatus status,
        BigDecimal baseSalary,
        String currency,
        Contract.ContractType contractType,
        LocalDate contractStartDate,
        LocalDate contractEndDate,
        UUID departmentId,
        String departmentName,
        String positionTitle,
        int leaveBalance,
        String photoUrl,
        String qrCodeUrl,
        LocalDateTime createdAt
) {}

