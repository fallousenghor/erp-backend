package com.company.erp.modules.finance.domain.model;

import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import com.company.erp.modules.finance.domain.model.valueobject.TaxRate;
import com.company.erp.shared.base.BaseAuditEntity;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Invoice Aggregate Root.
 * Manages line items, totals, tax calculation, and status transitions.
 */
@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseAuditEntity {

    @Column(name = "invoice_number", nullable = false, unique = true, length = 30)
    private String invoiceNumber;

    @Column(name = "client_name", nullable = false, length = 200)
    private String clientName;

    @Column(name = "client_email", length = 150)
    private String clientEmail;

    @Column(name = "client_address", length = 500)
    private String clientAddress;

    // Cross-module ref by ID
    @Column(name = "issued_by_id")
    private UUID issuedById;

    @Column(name = "issued_by_name", length = 160)
    private String issuedByName;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private InvoiceStatus status = InvoiceStatus.DRAFT;

    @Column(name = "currency", nullable = false, length = 3)
    @Builder.Default
    private String currency = "XOF";

    @Column(name = "subtotal", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "notes", length = 1000)
    private String notes;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void addItem(String description, int quantity, BigDecimal unitPrice) {
        ensureEditable();
        InvoiceItem item = InvoiceItem.builder()
                .invoice(this)
                .description(description)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .lineTotal(unitPrice.multiply(BigDecimal.valueOf(quantity)))
                .build();
        this.items.add(item);
        recalculate();
    }

    public void setTaxRate(TaxRate rate) {
        ensureEditable();
        this.taxRate = rate.percentage();
        recalculate();
    }

    public void send() {
        transitionTo(InvoiceStatus.SENT);
    }

    public void markPaid() {
        transitionTo(InvoiceStatus.PAID);
    }

    public void cancel() {
        transitionTo(InvoiceStatus.CANCELLED);
    }

    public void addPayment(Payment payment) {
        if (this.status != InvoiceStatus.SENT) {
            throw new BusinessException(ErrorCode.INVOICE_INVALID_TRANSITION,
                    "Can only add payments to SENT invoices");
        }
        this.payments.add(payment);
    }

    private void transitionTo(InvoiceStatus next) {
        if (!this.status.canTransitionTo(next)) {
            throw new BusinessException(ErrorCode.INVOICE_INVALID_TRANSITION,
                    "Cannot transition from " + this.status + " to " + next);
        }
        this.status = next;
    }

    private void ensureEditable() {
        if (this.status != InvoiceStatus.DRAFT) {
            throw new BusinessException(ErrorCode.INVOICE_INVALID_TRANSITION,
                    "Invoice can only be edited in DRAFT status");
        }
    }

    private void recalculate() {
        this.subtotal = items.stream()
                .map(InvoiceItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.taxAmount = TaxRate.of(this.taxRate.doubleValue()).applyTo(this.subtotal);
        this.total = this.subtotal.add(this.taxAmount);
    }
}
