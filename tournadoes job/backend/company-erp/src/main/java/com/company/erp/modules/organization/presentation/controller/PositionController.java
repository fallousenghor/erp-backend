package com.company.erp.modules.organization.presentation.controller;

import com.company.erp.modules.organization.application.dto.response.PositionResponse;
import com.company.erp.modules.organization.application.service.PositionService;
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
@RequestMapping("/api/v1/positions")
@Tag(name = "Positions", description = "Job positions within departments")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    @Operation(summary = "Create a new position in a department")
    public ResponseEntity<ApiResponse<PositionResponse>> create(
            @RequestParam UUID departmentId,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary) {
        return ResponseEntity.status(201).body(
                ApiResponse.created(positionService.create(
                        departmentId, title, description, minSalary, maxSalary)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get position by ID")
    public ResponseEntity<ApiResponse<PositionResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(positionService.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List positions by department")
    public ResponseEntity<ApiResponse<List<PositionResponse>>> findByDepartment(
            @RequestParam UUID departmentId) {
        return ResponseEntity.ok(ApiResponse.success(positionService.findByDepartment(departmentId)));
    }
}
