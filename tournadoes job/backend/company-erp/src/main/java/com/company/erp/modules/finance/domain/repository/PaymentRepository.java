package com.company.erp.modules.finance.domain.repository;

import com.company.erp.modules.finance.domain.model.Payment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(UUID id);
    List<Payment> findByInvoiceId(UUID invoiceId);
}
