package com.company.erp.modules.finance.infrastructure;

import com.company.erp.modules.finance.infrastructure.persistence.InvoiceJpaRepository;
import com.company.erp.shared.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private InvoiceJpaRepository invoiceJpaRepository;

    @Test
    void initialInvoiceCountIsZero() {
        assertEquals(0, invoiceJpaRepository.count());
    }
}
