package com.company.erp.modules.finance.domain.repository;

import com.company.erp.modules.finance.domain.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    Expense save(Expense expense);
    void delete(Expense expense);
    Optional<Expense> findById(UUID id);
    Page<Expense> findAll(Specification<Expense> spec, Pageable pageable);
    Page<Expense> findAll(Pageable pageable);
    BigDecimal sumAmountByStatus(Expense.ExpenseStatus status);
    long countByStatus(Expense.ExpenseStatus status);
}
