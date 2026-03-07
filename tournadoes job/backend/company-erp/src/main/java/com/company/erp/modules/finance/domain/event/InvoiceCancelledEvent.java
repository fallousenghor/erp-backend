package com.company.erp.modules.finance.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class InvoiceCancelledEvent extends BaseDomainEvent {
    private final UUID invoiceId;
    private final String invoiceNumber;

    public InvoiceCancelledEvent(UUID invoiceId, String invoiceNumber) {
        super("INVOICE_CANCELLED");
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
    }
}
