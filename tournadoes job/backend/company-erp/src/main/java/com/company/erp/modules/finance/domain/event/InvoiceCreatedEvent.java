package com.company.erp.modules.finance.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class InvoiceCreatedEvent extends BaseDomainEvent {
    private final UUID invoiceId;
    private final String invoiceNumber;
    private final BigDecimal total;

    public InvoiceCreatedEvent(UUID invoiceId, String invoiceNumber, BigDecimal total) {
        super("INVOICE_CREATED");
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.total = total;
    }
}
