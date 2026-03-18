package com.company.erp.modules.finance.application.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateExpenseRequest(
        @Size(max = 200) String title,
        String description,
        @Size(max = 100) String category,
        @DecimalMin("0.01") BigDecimal amount,
        String currency,
        LocalDate expenseDate,
        UUID departmentId,
        String receiptReference
) {}

