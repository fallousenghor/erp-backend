package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.response.AttendanceResponse;
import com.company.erp.modules.hr.application.service.AttendanceService;
import com.company.erp.modules.hr.domain.model.Attendance;
import com.company.erp.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/attendances")
@Tag(name = "Attendance", description = "HR — Attendance tracking")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> record(
            @RequestParam UUID employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOut,
            @RequestParam(defaultValue = "PRESENT") Attendance.AttendanceStatus status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                attendanceService.recordAttendance(employeeId, date, checkIn, checkOut, status, notes)));
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> update(
            @PathVariable UUID attendanceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkIn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOut,
            @RequestParam(required = false) Attendance.AttendanceStatus status,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(ApiResponse.success(
                attendanceService.updateAttendance(attendanceId, checkIn, checkOut, status, notes)));
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> getById(@PathVariable UUID attendanceId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.findById(attendanceId)));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> findByEmployee(
            @PathVariable UUID employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(ApiResponse.success(
                attendanceService.findByEmployeeAndRange(employeeId, from, to)));
    }
}
