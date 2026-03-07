package com.company.erp.modules.finance.application.dto.response;

import com.company.erp.modules.finance.domain.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentResponse(
        UUID id,
        UUID invoiceId,
        String invoiceNumber,
        BigDecimal amount,
        String currency,
        Payment.PaymentMethod paymentMethod,
        LocalDate paymentDate,
        String reference
) {}
