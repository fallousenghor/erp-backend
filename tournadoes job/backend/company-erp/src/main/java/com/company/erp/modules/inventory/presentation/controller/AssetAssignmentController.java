package com.company.erp.modules.inventory.presentation.controller;

import com.company.erp.modules.inventory.application.dto.response.AssetAssignmentResponse;
import com.company.erp.modules.inventory.application.service.AssetAssignmentService;
import com.company.erp.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/asset-assignments")
@Tag(name = "Asset Assignments", description = "Inventory — Assignment history")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AssetAssignmentController {

    private final AssetAssignmentService assignmentService;

    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get all asset assignments for an employee")
    public ResponseEntity<ApiResponse<List<AssetAssignmentResponse>>> findByEmployee(
            @PathVariable UUID employeeId) {
        return ResponseEntity.ok(ApiResponse.success(
                assignmentService.findByEmployee(employeeId)));
    }

    @GetMapping("/employee/{employeeId}/active")
    @Operation(summary = "Get active asset assignments for an employee")
    public ResponseEntity<ApiResponse<List<AssetAssignmentResponse>>> findActiveByEmployee(
            @PathVariable UUID employeeId) {
        return ResponseEntity.ok(ApiResponse.success(
                assignmentService.findActiveByEmployee(employeeId)));
    }
}
