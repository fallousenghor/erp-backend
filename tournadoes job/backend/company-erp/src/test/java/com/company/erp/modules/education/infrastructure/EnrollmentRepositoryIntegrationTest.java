package com.company.erp.modules.education.infrastructure;

import com.company.erp.modules.education.infrastructure.persistence.EnrollmentJpaRepository;
import com.company.erp.shared.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class EnrollmentRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private EnrollmentJpaRepository enrollmentJpaRepository;

    @Test
    void initialEnrollmentCountIsZero() {
        assertEquals(0, enrollmentJpaRepository.count());
    }
}
