package com.company.erp.modules.finance.infrastructure.persistence;

import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import com.company.erp.modules.finance.domain.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoiceJpaRepository jpaRepository;

    @Override public Invoice save(Invoice invoice)                      { return jpaRepository.save(invoice); }
    @Override public Optional<Invoice> findByInvoiceNumber(String num) { return jpaRepository.findByInvoiceNumber(num); }
    @Override public BigDecimal sumTotalByStatus(InvoiceStatus status)  { return jpaRepository.sumTotalByStatus(status); }
    @Override public long countByStatus(InvoiceStatus status)          { return jpaRepository.countByStatus(status); }

    @Override
    public Optional<Invoice> findById(UUID id) {
        return jpaRepository.findByIdWithDetails(id);
    }

    @Override
    public Page<Invoice> findAll(Specification<Invoice> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }
}
