package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.Objective;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ObjectiveJpaRepository extends JpaRepository<Objective, UUID> {
    Page<Objective> findAll(Pageable pageable);
    Page<Objective> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<Objective> findByStatus(Objective.ObjectiveStatus status, Pageable pageable);
    long count();
}

