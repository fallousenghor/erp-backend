package com.company.erp.modules.finance.domain.repository;

import com.company.erp.modules.finance.domain.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    Expense save(Expense expense);
    Optional<Expense> findById(UUID id);
    Page<Expense> findAll(Pageable pageable);
    BigDecimal sumAmountByStatus(Expense.ExpenseStatus status);
}
