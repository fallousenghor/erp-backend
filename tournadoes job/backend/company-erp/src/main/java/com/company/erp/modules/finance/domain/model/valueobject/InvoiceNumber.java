package com.company.erp.modules.finance.domain.model.valueobject;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * InvoiceNumber Value Object — auto-generated, format: INV-YYYY-NNNNNN
 */
public record InvoiceNumber(String value) {

    private static final AtomicInteger sequence = new AtomicInteger(1);

    public static InvoiceNumber generate() {
        String year = String.valueOf(LocalDate.now().getYear());
        String seq  = String.format("%06d", sequence.getAndIncrement());
        return new InvoiceNumber("INV-" + year + "-" + seq);
    }

    public static InvoiceNumber of(String value) {
        return new InvoiceNumber(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
