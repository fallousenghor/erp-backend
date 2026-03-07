package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.request.LeaveRequestDto;
import com.company.erp.modules.hr.application.dto.response.LeaveRequestResponse;
import com.company.erp.modules.hr.application.service.LeaveService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/leave-requests")
@Tag(name = "Leave Requests", description = "HR — Leave management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;

    @PostMapping("/employee/{employeeId}")
    @Operation(summary = "Submit a leave request for an employee")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> submit(
            @PathVariable UUID employeeId,
            @Valid @RequestBody LeaveRequestDto dto) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(leaveService.submit(employeeId, dto)));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a pending leave request")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.approve(id)));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a pending leave request")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> reject(
            @PathVariable UUID id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success(leaveService.reject(id, reason)));
    }

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all leave requests for an employee")
    public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> findByEmployee(
            @PathVariable UUID employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                leaveService.findByEmployee(employeeId, PageRequest.of(page, size))));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending leave requests")
    public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> findPending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                leaveService.findPending(PageRequest.of(page, size))));
    }

    @GetMapping
    @Operation(summary = "Get all leave requests (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<LeaveRequestResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                leaveService.findAll(PageRequest.of(page, size))));
    }

    @GetMapping("/stats/by-type")
    @Operation(summary = "Get leave distribution statistics by type")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getStatsByType() {
        return ResponseEntity.ok(ApiResponse.success(leaveService.getStatsByType()));
    }

    @GetMapping("/balances")
    @Operation(summary = "Get leave balances per employee")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getBalancesByEmployee() {
        return ResponseEntity.ok(ApiResponse.success(leaveService.getBalancesByEmployee()));
    }
}
