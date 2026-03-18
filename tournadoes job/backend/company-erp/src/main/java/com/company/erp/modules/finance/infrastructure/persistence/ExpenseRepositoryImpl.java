package com.company.erp.modules.finance.infrastructure.persistence;

import com.company.erp.modules.finance.domain.model.Expense;
import com.company.erp.modules.finance.domain.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepository {

    private final ExpenseJpaRepository jpaRepository;

    @Override public Expense save(Expense e)                              { return jpaRepository.save(e); }
    @Override public void delete(Expense e)                              { jpaRepository.delete(e); }
    @Override public Optional<Expense> findById(UUID id)               { return jpaRepository.findById(id); }
    @Override public Page<Expense> findAll(Pageable pageable)          { return jpaRepository.findAll(pageable); }
    
    @Override
    public Page<Expense> findAll(Specification<Expense> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }
    
    @Override public BigDecimal sumAmountByStatus(Expense.ExpenseStatus s){ return jpaRepository.sumAmountByStatus(s); }
    @Override public long countByStatus(Expense.ExpenseStatus s)        { return jpaRepository.countByStatus(s); }
}
