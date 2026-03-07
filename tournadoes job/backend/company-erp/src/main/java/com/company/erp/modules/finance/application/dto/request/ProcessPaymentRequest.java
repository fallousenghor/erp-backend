package com.company.erp.modules.finance.application.dto.request;

import com.company.erp.modules.finance.domain.model.Payment;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProcessPaymentRequest(
        @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull Payment.PaymentMethod paymentMethod,
        @NotNull LocalDate paymentDate,
        String reference,
        String notes
) {}
