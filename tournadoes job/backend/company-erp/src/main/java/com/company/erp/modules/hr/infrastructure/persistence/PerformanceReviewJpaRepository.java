package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.PerformanceReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PerformanceReviewJpaRepository extends JpaRepository<PerformanceReview, UUID> {
    Page<PerformanceReview> findAll(Pageable pageable);
    Page<PerformanceReview> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<PerformanceReview> findByPeriod(String period, Pageable pageable);
    long count();
}

