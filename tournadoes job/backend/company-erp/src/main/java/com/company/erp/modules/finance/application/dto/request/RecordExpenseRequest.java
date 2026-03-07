package com.company.erp.modules.finance.application.dto.request;

import com.company.erp.modules.finance.domain.model.Expense;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record RecordExpenseRequest(
        @NotBlank String title,
        String description,
        @NotNull Expense.ExpenseCategory category,
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotBlank @Size(min=3,max=3) String currency,
        @NotNull LocalDate expenseDate,
        UUID departmentId,
        String receiptReference
) {}
