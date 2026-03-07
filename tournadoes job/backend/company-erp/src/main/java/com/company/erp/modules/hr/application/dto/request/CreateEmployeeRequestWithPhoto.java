package com.company.erp.modules.hr.application.dto.request;

import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateEmployeeRequestWithPhoto(
        @NotBlank(message = "First name is required") String firstName,
        @NotBlank(message = "Last name is required") String lastName,
        @NotBlank @Email String email,
        String phone,
        LocalDate birthDate,
        LocalDate hireDate,
        UUID departmentId,
        String departmentName,
        UUID positionId,
        String positionTitle,
        BigDecimal baseSalary,
        String currency,
        Contract.ContractType contractType,
        LocalDate contractStartDate,
        LocalDate contractEndDate,
        MultipartFile photo
) {}

