package com.company.erp.modules.hr.application.dto.request;

import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateEmployeeRequest(
        @Size(min=2,max=80) String firstName,
        @Size(min=2,max=80) String lastName,
        @Email String email,
        String phone,
        UUID departmentId,
        String departmentName,
        UUID positionId,
        String positionTitle,
        BigDecimal baseSalary,
        String currency,
        Contract.ContractType contractType,
        LocalDate contractEndDate
) {}
