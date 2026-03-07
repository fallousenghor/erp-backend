package com.company.erp.modules.finance.infrastructure.persistence;

import com.company.erp.modules.finance.domain.model.Payment;
import com.company.erp.modules.finance.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;

    @Override public Payment save(Payment p)                      { return jpaRepository.save(p); }
    @Override public Optional<Payment> findById(UUID id)          { return jpaRepository.findById(id); }
    @Override public List<Payment> findByInvoiceId(UUID invoiceId){ return jpaRepository.findByInvoiceId(invoiceId); }
}
