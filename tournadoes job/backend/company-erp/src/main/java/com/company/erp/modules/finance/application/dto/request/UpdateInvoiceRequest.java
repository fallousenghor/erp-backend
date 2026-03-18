package com.company.erp.modules.finance.application.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UpdateInvoiceRequest(
        @Size(max = 200) String clientName,
        @Email String clientEmail,
        String clientAddress,
        LocalDate issueDate,
        LocalDate dueDate,
        @Size(max = 3) String currency,
        @DecimalMin("0") @DecimalMax("100") BigDecimal taxRate,
        String notes,
        List<ItemRequest> items
) {
    public record ItemRequest(
            @NotBlank String description,
            @Min(1) int quantity,
            @NotNull @DecimalMin("0.01") BigDecimal unitPrice
    ) {}
}

