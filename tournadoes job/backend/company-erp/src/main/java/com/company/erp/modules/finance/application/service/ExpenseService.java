package com.company.erp.modules.finance.application.service;

import com.company.erp.modules.finance.application.dto.request.RecordExpenseRequest;
import com.company.erp.modules.finance.application.dto.response.ExpenseResponse;
import com.company.erp.modules.finance.application.mapper.ExpenseMapper;
import com.company.erp.modules.finance.domain.event.ExpenseRecordedEvent;
import com.company.erp.modules.finance.domain.model.Expense;
import com.company.erp.modules.finance.domain.model.valueobject.Money;
import com.company.erp.modules.finance.domain.repository.ExpenseRepository;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;
    private final DomainEventPublisher eventPublisher;

    @Auditable(action = "RECORD_EXPENSE", entity = "Expense")
    @PreAuthorize("hasPermission(null, 'invoice:create')")
    public ExpenseResponse record(RecordExpenseRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

        Expense expense = Expense.builder()
                .title(request.title())
                .description(request.description())
                .category(request.category())
                .amount(Money.of(request.amount(), request.currency()))
                .expenseDate(request.expenseDate())
                .submittedByName(currentUser)
                .departmentId(request.departmentId())
                .receiptReference(request.receiptReference())
                .build();

        expense = expenseRepository.save(expense);
        eventPublisher.publish(new ExpenseRecordedEvent(
                expense.getId(), expense.getTitle(), expense.getAmount().getAmount()));

        return expenseMapper.toResponse(expense);
    }

    @Auditable(action = "APPROVE_EXPENSE", entity = "Expense")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public ExpenseResponse approve(UUID id) {
        Expense expense = findOrThrow(id);
        String approver = SecurityContextHolder.getContext().getAuthentication().getName();
        expense.approve(approver);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public PageResponse<ExpenseResponse> findAll(Pageable pageable) {
        return PageResponse.from(expenseRepository.findAll(pageable).map(expenseMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public ExpenseResponse findById(UUID id) {
        return expenseMapper.toResponse(findOrThrow(id));
    }

    private Expense findOrThrow(UUID id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EXPENSE_NOT_FOUND, id));
    }
}
