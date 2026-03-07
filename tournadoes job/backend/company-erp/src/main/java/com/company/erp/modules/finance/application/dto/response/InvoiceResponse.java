package com.company.erp.modules.finance.application.dto.response;

import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        String invoiceNumber,
        String clientName,
        String clientEmail,
        String issuedByName,
        LocalDate issueDate,
        LocalDate dueDate,
        InvoiceStatus status,
        String currency,
        BigDecimal subtotal,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal total,
        String notes,
        List<ItemResponse> items,
        LocalDateTime createdAt
) {
    public record ItemResponse(
            UUID id,
            String description,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}
}
