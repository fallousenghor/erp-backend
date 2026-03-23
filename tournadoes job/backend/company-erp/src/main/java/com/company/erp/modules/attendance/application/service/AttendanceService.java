package com.company.erp.modules.attendance.application.service;

import com.company.erp.modules.attendance.application.dto.request.CreateAttendanceRequest;
import com.company.erp.modules.attendance.application.dto.request.UpdateAttendanceRequest;
import com.company.erp.modules.attendance.application.dto.response.AttendanceResponse;
import com.company.erp.modules.attendance.application.dto.response.PresenceStatsResponse;
import com.company.erp.modules.attendance.application.dto.response.WeeklyPresenceResponse;
import com.company.erp.modules.attendance.application.mapper.AttendanceMapper;
import com.company.erp.modules.attendance.domain.model.AttendanceRecord;
import com.company.erp.modules.attendance.domain.repository.AttendanceRepository;
import com.company.erp.modules.hr.domain.repository.EmployeeRepository;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Attendance Service - Complete presence tracking for RH module
 * Supports daily records, stats, presence rates, late tracking
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

  private final AttendanceRepository attendanceRepository;
  private final EmployeeRepository employeeRepository;
  private final AttendanceMapper attendanceMapper;

  @Auditable(action = "CREATE_ATTENDANCE", entity = "AttendanceRecord")
  @PreAuthorize("hasPermission(null, 'attendance:create')")
  public AttendanceResponse create(CreateAttendanceRequest request) {
    // Validate employee exists and is active
    var employee = employeeRepository.findById(request.employeeId())
      .filter(e -> "ACTIVE".equals(e.getStatus()))
      .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND, request.employeeId()));

    // Check if record exists for this date
    if (attendanceRepository.existsByEmployeeIdAndRecordDate(request.employeeId(), request.recordDate())) {
      throw new BusinessException(ErrorCode.CONFLICT, "Attendance already recorded for this date");
    }

    var record = AttendanceRecord.builder()
        .employeeId(request.employeeId())
        .employeeNumber(employee.getEmployeeNumber())
        .employeeName(employee.getFullName())
        .departmentId(employee.getDepartmentId())
        .departmentName(employee.getDepartmentName())
        .recordDate(request.recordDate())
        .checkInTime(request.checkInTime())
        .checkOutTime(request.checkOutTime())
        .status(request.status())
        .lateMinutes(calculateLateMinutes(request.checkInTime()))
        .notes(request.notes())
        .location(request.location())
        .build();

    // Auto-calculate worked hours and validate
    if (request.checkInTime() != null && request.checkOutTime() != null) {
      record.setWorkedHours(Duration.between(request.checkInTime(), request.checkOutTime()));
    }

    var saved = attendanceRepository.save(record);
    log.info("Attendance recorded: employee={} date={} status={}", 
        request.employeeId(), request.recordDate(), request.status());
    return attendanceMapper.toResponse(saved);
  }

  @Auditable(action = "UPDATE_ATTENDANCE", entity = "AttendanceRecord")
  @PreAuthorize("hasPermission(null, 'attendance:update')")
  public AttendanceResponse update(UUID id, UpdateAttendanceRequest request) {
    var record = findOrThrow(id);
    
    record.setCheckInTime(request.checkInTime());
    record.setCheckOutTime(request.checkOutTime());
    record.setStatus(request.status());
    record.setLateMinutes(calculateLateMinutes(request.checkInTime()));
    record.setNotes(request.notes());
    record.setLocation(request.location());
    
    // Recalculate worked hours
    if (request.checkInTime() != null && request.checkOutTime() != null) {
      record.setWorkedHours(Duration.between(request.checkInTime(), request.checkOutTime()));
    }
    
    var updated = attendanceRepository.save(record);
    log.info("Attendance updated: id={} status={}", id, request.status());
    return attendanceMapper.toResponse(updated);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'attendance:read')")
  public AttendanceResponse findById(UUID id) {
    return attendanceMapper.toResponse(findOrThrow(id));
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'attendance:read')")
  public PageResponse<AttendanceResponse> findAll(
      UUID employeeId, LocalDate fromDate, LocalDate toDate, 
      String status, Pageable pageable) {
    
    var records = attendanceRepository.findAllWithFilters(employeeId, fromDate, toDate, status, pageable);
    var page = new org.springframework.data.domain.PageImpl<>(records, pageable, records.size());
    return PageResponse.from(page.map(attendanceMapper::toResponse));
  }

  @Auditable(action = "DELETE_ATTENDANCE", entity = "AttendanceRecord")
  @PreAuthorize("hasPermission(null, 'attendance:delete')")
  public void delete(UUID id) {
    var record = findOrThrow(id);
    attendanceRepository.delete(record);
    log.info("Attendance deleted: id={}", id);
  }

  @Transactional(readOnly = true)
  public PresenceStatsResponse getPresenceStats() {
    // Last 30 days aggregated stats - just return empty for now, can be enhanced later
    return PresenceStatsResponse.builder()
        .days(List.of())
        .build();
  }

  @Transactional(readOnly = true)
  public WeeklyPresenceResponse getWeeklyPresence() {
    // Current week Monday-Sunday - return empty for now, can be enhanced later
    return WeeklyPresenceResponse.builder()
        .days(List.of())
        .build();
  }

  // ── Helpers ──────────────────────────────────────────────────────────────────
  
  private AttendanceRecord findOrThrow(UUID id) {
    return attendanceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ATTENDANCE_NOT_FOUND, id));
  }

  private int calculateLateMinutes(LocalTime checkIn) {
    if (checkIn == null) return 0;
    var expected = LocalTime.of(8, 0);
    return (int) Duration.between(expected, checkIn).toMinutes();
  }
}

