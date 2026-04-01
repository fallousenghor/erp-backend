package com.company.erp.modules.hr.domain.repository;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LeaveRepository extends JpaRepository<LeaveRequest, UUID> {

  Page<LeaveRequest> findByStatus(String status, Pageable pageable);
  Page<LeaveRequest> findByEmployeeId(UUID employeeId, Pageable pageable);
  
  @Query("""
    SELECT l FROM LeaveRequest l 
    WHERE (:employeeId IS NULL OR l.employeeId = :employeeId)
      AND (:fromDate IS NULL OR l.startDate >= :fromDate)
      AND (:toDate IS NULL OR l.endDate <= :toDate)
      AND (:status IS NULL OR l.status = :status)
      AND (:leaveType IS NULL OR l.leaveType = :leaveType)
    """)
  Page<LeaveRequest> findAllBy(
      @Param("employeeId") UUID employeeId, 
      @Param("fromDate") LocalDate fromDate, 
      @Param("toDate") LocalDate toDate, 
      @Param("status") String status, 
      @Param("leaveType") String leaveType, 
      Pageable pageable);

  @Query(value = """
    SELECT
      COALESCE(COUNT(CASE WHEN l.leave_type = 'ANNUAL' THEN 1 END), 0),
      COALESCE(COUNT(CASE WHEN l.leave_type = 'SICK' THEN 1 END), 0),
      COALESCE(COUNT(CASE WHEN l.leave_type = 'MATERNITY' THEN 1 END), 0),
      COALESCE(COUNT(CASE WHEN l.leave_type = 'UNPAID' THEN 1 END), 0),
      COALESCE(COUNT(CASE WHEN l.leave_type = 'EXCEPTIONAL' THEN 1 END), 0)
    FROM leave_requests l
    WHERE l.created_at >= CAST(:fromDate AS date)
    """, nativeQuery = true)
  Object[] getStatsByTypeRaw(@Param("fromDate") LocalDate fromDate);

  @Query(value = """
    SELECT 
      employee_id as "employeeId",
      leave_type as "leaveType", 
      SUM(days_requested) as "balance"
    FROM leave_requests
    WHERE status = 'APPROVED'
    GROUP BY employee_id, leave_type
    """, nativeQuery = true)
  List<Object[]> getLeaveBalances();

}
