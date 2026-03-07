package com.company.erp.modules.finance.domain.model;

import com.company.erp.modules.finance.domain.model.valueobject.Money;
import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Payment entity — belongs to Invoice aggregate.
 */
@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseAuditEntity {

    public enum PaymentMethod { BANK_TRANSFER, CASH, CHEQUE, MOBILE_MONEY, CARD }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Embedded
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "notes", length = 500)
    private String notes;
}
