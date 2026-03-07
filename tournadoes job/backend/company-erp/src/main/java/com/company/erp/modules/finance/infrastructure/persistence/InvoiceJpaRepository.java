package com.company.erp.modules.finance.infrastructure.persistence;

import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceJpaRepository
        extends JpaRepository<Invoice, UUID>, JpaSpecificationExecutor<Invoice> {

    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);

    @Query("SELECT COALESCE(SUM(i.total), 0) FROM Invoice i WHERE i.status = :status")
    BigDecimal sumTotalByStatus(@Param("status") InvoiceStatus status);

    long countByStatus(InvoiceStatus status);

    @Query("""
            SELECT i FROM Invoice i
            LEFT JOIN FETCH i.items
            LEFT JOIN FETCH i.payments
            WHERE i.id = :id
            """)
    Optional<Invoice> findByIdWithDetails(@Param("id") UUID id);
}
