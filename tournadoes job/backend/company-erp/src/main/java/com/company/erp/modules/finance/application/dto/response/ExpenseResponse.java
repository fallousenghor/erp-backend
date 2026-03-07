package com.company.erp.modules.finance.application.dto.response;

import com.company.erp.modules.finance.domain.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        String title,
        String description,
        Expense.ExpenseCategory category,
        BigDecimal amount,
        String currency,
        LocalDate expenseDate,
        Expense.ExpenseStatus status,
        String submittedByName,
        String approvedBy,
        UUID departmentId,
        String receiptReference,
        LocalDateTime createdAt
) {}
