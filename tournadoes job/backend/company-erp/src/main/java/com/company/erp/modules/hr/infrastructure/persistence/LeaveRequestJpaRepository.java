package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeaveRequestJpaRepository extends JpaRepository<LeaveRequest, UUID> {

    Page<LeaveRequest> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<LeaveRequest> findByStatus(String status, Pageable pageable);

}
