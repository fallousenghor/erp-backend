package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.domain.model.valueobject.LeaveType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeaveRequestJpaRepository extends JpaRepository<LeaveRequest, UUID> {

    Page<LeaveRequest> findByEmployeeId(UUID employeeId, Pageable pageable);
    Page<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status, Pageable pageable);
    long countByStatus(LeaveRequest.LeaveStatus status);
    
    // Count by leave type (for approved leaves only)
    long countByLeaveTypeAndStatus(LeaveType leaveType, LeaveRequest.LeaveStatus status);
    
    // Sum of days requested by leave type (for approved leaves)
    @Query("SELECT COALESCE(SUM(l.daysRequested), 0) FROM LeaveRequest l WHERE l.leaveType = :leaveType AND l.status = :status")
    int sumDaysByLeaveTypeAndStatus(LeaveType leaveType, LeaveRequest.LeaveStatus status);
}
