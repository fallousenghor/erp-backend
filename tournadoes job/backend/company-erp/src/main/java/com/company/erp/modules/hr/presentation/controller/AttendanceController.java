package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.response.AttendanceResponse;
import com.company.erp.modules.hr.application.service.HrAttendanceService;
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

@RestController("hrAttendanceController")
@RequestMapping("/api/v1/attendances")
@Tag(name = "Attendance", description = "HR — Attendance tracking")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AttendanceController {

    private final HrAttendanceService attendanceService;

    @PostMapping
    public ResponseEntity<ApiResponse<AttendanceResponse>> record(
            @RequestBody RecordAttendanceRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                attendanceService.recordAttendance(
                    request.employeeId(), 
                    request.date(), 
                    request.checkIn(), 
                    request.checkOut(), 
                    request.status(), 
                    request.notes())));
    }

    @PutMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendanceResponse>> update(
            @PathVariable UUID attendanceId,
            @RequestBody UpdateAttendanceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                attendanceService.updateAttendance(
                    attendanceId, 
                    request.checkIn(), 
                    request.checkOut(), 
                    request.status(), 
                    request.notes())));
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
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> findAll() {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.findAll()));
    }
    
    // Request DTOs
    public record RecordAttendanceRequest(
        UUID employeeId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkIn,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOut,
        Attendance.AttendanceStatus status,
        String notes
    ) {}
    
    public record UpdateAttendanceRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkIn,
        @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime checkOut,
        Attendance.AttendanceStatus status,
        String notes
    ) {}
}
