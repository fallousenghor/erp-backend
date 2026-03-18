package com.company.erp.modules.finance.application.service;

import com.company.erp.modules.finance.application.dto.request.RecordExpenseRequest;
import com.company.erp.modules.finance.application.dto.request.UpdateExpenseRequest;
import com.company.erp.modules.finance.application.dto.response.ExpenseResponse;
import com.company.erp.modules.finance.application.dto.response.ExpenseSummaryResponse;
import com.company.erp.modules.finance.application.mapper.ExpenseMapper;
import com.company.erp.modules.finance.domain.event.ExpenseRecordedEvent;
import com.company.erp.modules.finance.domain.model.Expense;
import com.company.erp.modules.finance.domain.model.valueobject.Money;
import com.company.erp.modules.finance.domain.repository.ExpenseRepository;
import com.company.erp.modules.finance.infrastructure.persistence.specification.ExpenseSpecification;
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

import java.math.BigDecimal;
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

    @Auditable(action = "UPDATE_EXPENSE", entity = "Expense")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public ExpenseResponse update(UUID id, UpdateExpenseRequest request) {
        Expense expense = findOrThrow(id);

        if (request.title() != null) {
            expense.setTitle(request.title());
        }
        if (request.description() != null) {
            expense.setDescription(request.description());
        }
        if (request.category() != null) {
            try {
                expense.setCategory(Expense.ExpenseCategory.valueOf(request.category().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // Keep existing category if invalid
            }
        }
        if (request.amount() != null) {
            expense.setAmount(Money.of(request.amount(), request.currency() != null ? request.currency() : "XOF"));
        }
        if (request.expenseDate() != null) {
            expense.setExpenseDate(request.expenseDate());
        }
        if (request.departmentId() != null) {
            expense.setDepartmentId(request.departmentId());
        }
        if (request.receiptReference() != null) {
            expense.setReceiptReference(request.receiptReference());
        }

        Expense saved = expenseRepository.save(expense);
        return expenseMapper.toResponse(saved);
    }

    @Auditable(action = "DELETE_EXPENSE", entity = "Expense")
    @PreAuthorize("hasPermission(null, 'invoice:delete')")
    public void delete(UUID id) {
        Expense expense = findOrThrow(id);
        expenseRepository.delete(expense);
    }

    @Auditable(action = "APPROVE_EXPENSE", entity = "Expense")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public ExpenseResponse approve(UUID id) {
        Expense expense = findOrThrow(id);
        String approver = SecurityContextHolder.getContext().getAuthentication().getName();
        expense.approve(approver);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    @Auditable(action = "REJECT_EXPENSE", entity = "Expense")
    @PreAuthorize("hasPermission(null, 'invoice:update')")
    public ExpenseResponse reject(UUID id) {
        Expense expense = findOrThrow(id);
        String approver = SecurityContextHolder.getContext().getAuthentication().getName();
        expense.reject(approver);
        return expenseMapper.toResponse(expenseRepository.save(expense));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public PageResponse<ExpenseResponse> findAll(String category, String status, Pageable pageable) {
        return PageResponse.from(
                expenseRepository.findAll(
                        ExpenseSpecification.build(category, status), pageable)
                        .map(expenseMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public ExpenseResponse findById(UUID id) {
        return expenseMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'invoice:read')")
    public ExpenseSummaryResponse getSummary() {
        BigDecimal totalPending = expenseRepository.sumAmountByStatus(Expense.ExpenseStatus.PENDING);
        BigDecimal totalApproved = expenseRepository.sumAmountByStatus(Expense.ExpenseStatus.APPROVED);
        BigDecimal totalPaid = expenseRepository.sumAmountByStatus(Expense.ExpenseStatus.PAID);
        BigDecimal totalRejected = expenseRepository.sumAmountByStatus(Expense.ExpenseStatus.REJECTED);

        totalPending = totalPending != null ? totalPending : BigDecimal.ZERO;
        totalApproved = totalApproved != null ? totalApproved : BigDecimal.ZERO;
        totalPaid = totalPaid != null ? totalPaid : BigDecimal.ZERO;
        totalRejected = totalRejected != null ? totalRejected : BigDecimal.ZERO;

        return new ExpenseSummaryResponse(
                totalPending,
                totalApproved,
                totalPaid,
                totalRejected,
                totalPending.add(totalApproved).add(totalPaid),
                expenseRepository.countByStatus(Expense.ExpenseStatus.PENDING),
                expenseRepository.countByStatus(Expense.ExpenseStatus.APPROVED),
                expenseRepository.countByStatus(Expense.ExpenseStatus.PAID),
                expenseRepository.countByStatus(Expense.ExpenseStatus.REJECTED),
                expenseRepository.countByStatus(Expense.ExpenseStatus.PENDING)
                        + expenseRepository.countByStatus(Expense.ExpenseStatus.APPROVED)
                        + expenseRepository.countByStatus(Expense.ExpenseStatus.PAID)
                        + expenseRepository.countByStatus(Expense.ExpenseStatus.REJECTED)
        );
    }

    private Expense findOrThrow(UUID id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EXPENSE_NOT_FOUND, id));
    }
}
