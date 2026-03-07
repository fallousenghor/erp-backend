package com.company.erp.modules.finance.domain.model;

import com.company.erp.modules.finance.domain.model.valueobject.Money;
import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Expense Aggregate Root.
 */
@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense extends BaseAuditEntity {

    public enum ExpenseCategory {
        SALARY, RENT, UTILITIES, SUPPLIES, TRAVEL,
        TRAINING, MAINTENANCE, MARKETING, OTHER
    }

    public enum ExpenseStatus { PENDING, APPROVED, REJECTED, PAID }

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ExpenseCategory category;

    @Embedded
    private Money amount;

    @Column(name = "expense_date", nullable = false)
    private LocalDate expenseDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ExpenseStatus status = ExpenseStatus.PENDING;

    @Column(name = "submitted_by_id")
    private UUID submittedById;

    @Column(name = "submitted_by_name", length = 160)
    private String submittedByName;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "receipt_reference", length = 100)
    private String receiptReference;

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void approve(String approvedBy) {
        this.status = ExpenseStatus.APPROVED;
        this.approvedBy = approvedBy;
    }

    public void reject(String approvedBy) {
        this.status = ExpenseStatus.REJECTED;
        this.approvedBy = approvedBy;
    }

    public void markPaid() {
        if (this.status != ExpenseStatus.APPROVED) {
            throw new com.company.erp.shared.exception.BusinessException(
                    com.company.erp.shared.exception.ErrorCode.BAD_REQUEST,
                    "Only approved expenses can be marked as paid");
        }
        this.status = ExpenseStatus.PAID;
    }
}
