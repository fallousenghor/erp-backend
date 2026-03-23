package com.company.erp.modules.attendance.presentation.controller;

import com.company.erp.modules.attendance.application.dto.request.CreateAttendanceRequest;
import com.company.erp.modules.attendance.application.dto.request.UpdateAttendanceRequest;
import com.company.erp.modules.attendance.application.dto.response.AttendanceResponse;
import com.company.erp.modules.attendance.application.dto.response.PresenceStatsResponse;
import com.company.erp.modules.attendance.application.dto.response.WeeklyPresenceResponse;
import com.company.erp.modules.attendance.application.service.AttendanceService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController("attendanceController")
@RequestMapping("/api/v1/attendance")
@Tag(name = "Attendance (Présence)", description = "Daily presence tracking - RH module")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AttendanceController {

  private final AttendanceService attendanceService;

  @PostMapping
  @Operation(summary = "Create attendance record")
  @PreAuthorize("hasPermission(null, 'attendance:create')")
  public ResponseEntity<ApiResponse<AttendanceResponse>> create(
      @Valid @RequestBody CreateAttendanceRequest request) {
    return ResponseEntity.status(201)
        .body(ApiResponse.created(attendanceService.create(request)));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get attendance record by ID")
  @PreAuthorize("hasPermission(null, 'attendance:read')")
  public ResponseEntity<ApiResponse<AttendanceResponse>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(attendanceService.findById(id)));
  }

  @GetMapping
  @Operation(summary = "List attendance records with filters")
  @PreAuthorize("hasPermission(null, 'attendance:read')")
  public ResponseEntity<ApiResponse<PageResponse<AttendanceResponse>>> findAll(
      @RequestParam(required = false) UUID employeeId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
      @RequestParam(required = false) String status,
      Pageable pageable) {
    var page = attendanceService.findAll(employeeId, fromDate, toDate, status, pageable);
    return ResponseEntity.ok(ApiResponse.success(page));
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update attendance record")
  @PreAuthorize("hasPermission(null, 'attendance:update')")
  public ResponseEntity<ApiResponse<AttendanceResponse>> update(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateAttendanceRequest request) {
    return ResponseEntity.ok(ApiResponse.success(attendanceService.update(id, request)));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete attendance record")
  @PreAuthorize("hasPermission(null, 'attendance:delete')")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
    attendanceService.delete(id);
    return ResponseEntity.ok(ApiResponse.noContent());
  }

  @GetMapping("/stats")
  @Operation(summary = "Get presence statistics (last 30 days)")
  @PreAuthorize("hasPermission(null, 'attendance:read')")
  public ResponseEntity<ApiResponse<PresenceStatsResponse>> getPresenceStats() {
    return ResponseEntity.ok(ApiResponse.success(attendanceService.getPresenceStats()));
  }

  @GetMapping("/weekly")
  @Operation(summary = "Get weekly presence data (current week)")
  @PreAuthorize("hasPermission(null, 'attendance:read')")
  public ResponseEntity<ApiResponse<WeeklyPresenceResponse>> getWeeklyPresence() {
    return ResponseEntity.ok(ApiResponse.success(attendanceService.getWeeklyPresence()));
  }
}

