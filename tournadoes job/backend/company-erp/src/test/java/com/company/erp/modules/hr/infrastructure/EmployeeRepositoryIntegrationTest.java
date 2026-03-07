package com.company.erp.modules.hr.infrastructure;

import com.company.erp.modules.hr.infrastructure.persistence.EmployeeJpaRepository;
import com.company.erp.shared.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private EmployeeJpaRepository employeeJpaRepository;

    @Test
    void initialEmployeeCountIsZero() {
        assertEquals(0, employeeJpaRepository.count());
    }
}
