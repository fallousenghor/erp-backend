package com.company.erp.modules.attendance.domain.repository;

import com.company.erp.modules.attendance.domain.model.AttendanceRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceRepositoryFixed extends JpaRepository<AttendanceRecord, UUID> {

  boolean existsByEmployeeIdAndRecordDate(UUID employeeId, LocalDate recordDate);

  List<AttendanceRecord> findAllByEmployeeIdAndRecordDateBetweenOrderByRecordDate(
      UUID employeeId, LocalDate fromDate, LocalDate toDate);

  @Query(value = """
    select ar.* from attendance_records ar 
    where (cast(?1 as uuid) is null or ar.employee_id = cast(?1 as uuid))
      and (cast(?2 as date) is null or ar.record_date >= cast(?2 as date))
      and (cast(?3 as date) is null or ar.record_date <= cast(?3 as date))
      and (cast(?4 as text) is null or ar.status ilike cast(?4 as text))
    order by ar.record_date desc
    """, 
    countQuery = """
    select count(*) from attendance_records ar 
    where (cast(?1 as uuid) is null or ar.employee_id = cast(?1 as uuid))
      and (cast(?2 as date) is null or ar.record_date >= cast(?2 as date))
      and (cast(?3 as date) is null or ar.record_date <= cast(?3 as date))
      and (cast(?4 as text) is null or ar.status ilike cast(?4 as text))
    """, 
    nativeQuery = true)
  Page<AttendanceRecord> findAllWithFilters(
      UUID employeeId,
      LocalDate fromDate,
      LocalDate toDate,
      String status,
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
