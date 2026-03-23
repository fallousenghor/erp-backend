package com.company.erp.modules.hr.domain.repository;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface LeaveRequestRepository {

    LeaveRequest save(LeaveRequest leaveRequest);

    Optional<LeaveRequest> findById(UUID id);

    Page<LeaveRequest> findByEmployeeId(UUID employeeId, Pageable pageable);

    Page<LeaveRequest> findByStatus(String status, Pageable pageable);

    Page<LeaveRequest> findAll(Pageable pageable);
}
