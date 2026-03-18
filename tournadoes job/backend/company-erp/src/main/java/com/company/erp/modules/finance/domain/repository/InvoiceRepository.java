package com.company.erp.modules.finance.domain.repository;

import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    void delete(Invoice invoice);
    Optional<Invoice> findById(UUID id);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    Page<Invoice> findAll(Specification<Invoice> spec, Pageable pageable);
    BigDecimal sumTotalByStatus(InvoiceStatus status);
    long countByStatus(InvoiceStatus status);
}
