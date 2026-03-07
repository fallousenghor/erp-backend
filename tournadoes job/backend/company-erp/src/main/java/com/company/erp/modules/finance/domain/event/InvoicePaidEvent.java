package com.company.erp.modules.finance.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class InvoicePaidEvent extends BaseDomainEvent {
    private final UUID invoiceId;
    private final String invoiceNumber;
    private final BigDecimal amount;

    public InvoicePaidEvent(UUID invoiceId, String invoiceNumber, BigDecimal amount) {
        super("INVOICE_PAID");
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
    }
}
