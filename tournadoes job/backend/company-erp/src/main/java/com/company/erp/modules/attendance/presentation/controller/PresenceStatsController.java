package com.company.erp.modules.attendance.presentation.controller;

import com.company.erp.modules.attendance.application.dto.response.PresenceStatsResponse;
import com.company.erp.modules.attendance.application.dto.response.WeeklyPresenceResponse;
import com.company.erp.modules.attendance.application.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Presence Stats Controller - Frontend RH Attendance charts
 * /v1/attendance/stats/stats, /v1/attendance/stats/weekly
 */
@Tag(name = "Attendance", description = "Presence Stats & Weekly Data")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
@PreAuthorize("isAuthenticated()")
public class PresenceStatsController {

  private final AttendanceService attendanceService;

  @Operation(summary = "Daily presence stats")
  @GetMapping("/stats")
  public ResponseEntity<PresenceStatsResponse> getPresenceStats() {
    return ResponseEntity.ok(attendanceService.getPresenceStats());
  }

  @Operation(summary = "Weekly presence data for chart")
  @GetMapping("/weekly")
  public ResponseEntity<WeeklyPresenceResponse> getWeeklyPresence() {
    return ResponseEntity.ok(attendanceService.getWeeklyPresence());
  }
}
