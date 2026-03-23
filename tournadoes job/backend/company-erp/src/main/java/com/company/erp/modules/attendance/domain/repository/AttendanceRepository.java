package com.company.erp.modules.attendance.domain.repository;

import com.company.erp.modules.attendance.domain.model.AttendanceRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, UUID> {

  boolean existsByEmployeeIdAndRecordDate(UUID employeeId, LocalDate recordDate);

  List<AttendanceRecord> findAllByEmployeeIdAndRecordDateBetweenOrderByRecordDate(
      UUID employeeId, LocalDate fromDate, LocalDate toDate);

  // Custom findAll with filters
  @Query("""
    SELECT a FROM AttendanceRecord a 
    WHERE (:employeeId IS NULL OR a.employeeId = :employeeId)
      AND (:fromDate IS NULL OR a.recordDate >= :fromDate)
      AND (:toDate IS NULL OR a.recordDate <= :toDate)
      AND (:status IS NULL OR a.status = :status)
    ORDER BY a.recordDate DESC
    """)
  List<AttendanceRecord> findAllWithFilters(
      @Param("employeeId") UUID employeeId,
      @Param("fromDate") LocalDate fromDate,
      @Param("toDate") LocalDate toDate,
      @Param("status") String status,
      Pageable pageable);

  // ── Stats Queries ──────────────────────────────────────────────────────────

  @Query(value = """
    SELECT 
      COUNT(a) as totalRecords,
      COUNT(CASE WHEN a.status = 'PRESENT' THEN 1 END)::float as presentDays,
      ROUND(AVG(CASE WHEN a.status = 'PRESENT' THEN 1.0 ELSE 0.0 END) * 100, 2) as presenceRate,
      ROUND(AVG(COALESCE(a.late_minutes, 0)), 1) as avgLateMinutes,
      ARRAY_AGG(
        jsonb_build_object(
          'date', a.record_date::date,
          'status', a.status,
          'lateMinutes', COALESCE(a.late_minutes, 0)
        ) ORDER BY a.record_date DESC
      ) as days
    FROM attendance_records a 
    WHERE a.record_date >= :fromDate 
      AND a.record_date <= CURRENT_DATE
    """, nativeQuery = true)
  Object[] getPresenceStats(@Param("fromDate") LocalDate fromDate);

  @Query(value = """
    SELECT 
      jsonb_build_object(
        'dayOfWeek', EXTRACT(dow FROM a.record_date),
        'date', a.record_date,
        'present', COUNT(CASE WHEN a.status = 'PRESENT' THEN 1 END),
        'absent', COUNT(CASE WHEN a.status = 'ABSENT' THEN 1 END),
        'late', COUNT(CASE WHEN a.status = 'LATE' THEN 1 END),
        'onLeave', COUNT(CASE WHEN a.status = 'ON_LEAVE' THEN 1 END),
        'remote', COUNT(CASE WHEN a.status = 'REMOTE' THEN 1 END),
        'total', COUNT(a)
      ) as dayStats
    FROM attendance_records a 
    WHERE a.record_date >= :startOfWeek 
      AND a.record_date <= :endOfWeek
    GROUP BY EXTRACT(dow FROM a.record_date), a.record_date
    ORDER BY a.record_date
    """, nativeQuery = true)
  List<Object[]> getWeeklyPresence(@Param("startOfWeek") LocalDate startOfWeek, @Param("endOfWeek") LocalDate endOfWeek);
}

