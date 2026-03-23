package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.request.CreateLeaveRequest;
import com.company.erp.modules.hr.application.dto.response.LeaveRequestResponse;
import com.company.erp.modules.hr.application.dto.response.LeaveStatsResponse;
import com.company.erp.modules.hr.application.service.LeaveService;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leave-requests")
@Tag(name = "Leaves (Congés)", description = "Leave requests workflow - RH module")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class LeaveController {

  private final LeaveService leaveService;

  @PostMapping
  @Operation(summary = "Create leave request")
  @PreAuthorize("hasPermission(null, 'leave:request')")
  public ResponseEntity<ApiResponse<LeaveRequestResponse>> create(
      @Valid @RequestBody CreateLeaveRequest request) {
    return ResponseEntity.status(201)
        .body(ApiResponse.created(leaveService.create(request)));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get leave request by ID")
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public ResponseEntity<ApiResponse<LeaveRequestResponse>> findById(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(leaveService.findById(id)));
  }

  @GetMapping
  @Operation(summary = "List leave requests")
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> findAll(
      @RequestParam(required = false) UUID employeeId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String leaveType,
      Pageable pageable) {
    var page = leaveService.findAll(employeeId, fromDate, toDate, status, leaveType, pageable);
    return ResponseEntity.ok(ApiResponse.success(page));
  }

  @GetMapping("/pending")
  @Operation(summary = "Get pending leave requests (for approval)")
  @PreAuthorize("hasPermission(null, 'leave:approve')")
  public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> getPending(Pageable pageable) {
    var page = leaveService.getPending(pageable);
    return ResponseEntity.ok(ApiResponse.success(page));
  }

  @GetMapping("/employee/{employeeId}")
  @Operation(summary = "Get leave requests by employee")
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> getByEmployee(
      @PathVariable UUID employeeId, Pageable pageable) {
    var page = leaveService.findByEmployee(employeeId, pageable);
    return ResponseEntity.ok(ApiResponse.success(page));
  }

  @PostMapping("/{id}/approve")
  @Operation(summary = "Approve leave request")
  @PreAuthorize("hasPermission(null, 'leave:approve')")
  public ResponseEntity<ApiResponse<LeaveRequestResponse>> approve(@PathVariable UUID id) {
    return ResponseEntity.ok(ApiResponse.success(leaveService.approve(id)));
  }

  @PostMapping("/{id}/reject")
  @Operation(summary = "Reject leave request")
  @PreAuthorize("hasPermission(null, 'leave:approve')")
  public ResponseEntity<ApiResponse<LeaveRequestResponse>> reject(
      @PathVariable UUID id, 
      @RequestParam String reason) {
    return ResponseEntity.ok(ApiResponse.success(leaveService.reject(id, reason)));
  }

  @GetMapping("/stats/by-type")
  @Operation(summary = "Get leave stats by type")
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public ResponseEntity<ApiResponse<LeaveStatsResponse>> getLeaveStatsByType() {
    return ResponseEntity.ok(ApiResponse.success(leaveService.getStatsByType()));
  }

  @GetMapping("/balances")
  @Operation(summary = "Get leave balances for all employees")
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public ResponseEntity<ApiResponse<List>> getLeaveBalances() {
    return ResponseEntity.ok(ApiResponse.success(leaveService.getLeaveBalances()));
  }
}
