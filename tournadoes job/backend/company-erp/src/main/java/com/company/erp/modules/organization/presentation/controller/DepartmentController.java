package com.company.erp.modules.organization.presentation.controller;

import com.company.erp.modules.organization.application.command.*;
import com.company.erp.modules.organization.application.dto.request.CreateDepartmentRequest;
import com.company.erp.modules.organization.application.dto.request.UpdateDepartmentRequest;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.modules.organization.application.dto.response.DepartmentResponse;
import com.company.erp.modules.organization.application.dto.response.DepartmentDetailResponse;
import com.company.erp.modules.organization.application.dto.response.DepartmentStatsResponse;
import com.company.erp.modules.organization.application.query.GetDepartmentsQuery;
import com.company.erp.modules.organization.application.service.DepartmentService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Departments", description = "Organization structure management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(
            @Valid @RequestBody CreateDepartmentRequest request) {
        CreateDepartmentCommand command = new CreateDepartmentCommand(
                request.name(), request.code(), request.description(), request.budget());
        return ResponseEntity.status(201)
                .body(ApiResponse.created(departmentService.create(command)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<ApiResponse<DepartmentResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.findById(id)));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get department detailed information with head history and budget")
    public ResponseEntity<ApiResponse<DepartmentDetailResponse>> findDetailById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.findDetailById(id)));
    }

    @GetMapping("/{id}/stats")
    @Operation(summary = "Get department statistics and metrics")
    public ResponseEntity<ApiResponse<DepartmentStatsResponse>> getStats(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.getStats(id)));
    }

    @GetMapping
    @Operation(summary = "List departments with filtering and pagination")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentResponse>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) BigDecimal minBudget,
            @RequestParam(required = false) BigDecimal maxBudget,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy) {
        GetDepartmentsQuery query = new GetDepartmentsQuery(
                name, code, active, minBudget, maxBudget,
                PageRequest.of(page, size, Sort.by(sortBy)));
        return ResponseEntity.ok(ApiResponse.success(departmentService.findAll(query)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDepartmentRequest request) {
        // Ensure name is provided for update
        if (request.name() == null || request.name().trim().isBlank()) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Department name is required");
        }
        UpdateDepartmentCommand command = new UpdateDepartmentCommand(
                id, request.name().trim(), request.description(), request.active());
        
        DepartmentResponse response = departmentService.update(command);
        
        // Handle budget update if provided
        if (request.budget() != null) {
            response = departmentService.updateBudget(id, request.budget());
        }
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/{id}/budget")
    @Operation(summary = "Update department budget")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateBudget(
            @PathVariable UUID id,
            @RequestParam BigDecimal budget) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.updateBudget(id, budget)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft-delete a department")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        departmentService.delete(new DeleteDepartmentCommand(id));
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @PostMapping("/{id}/restore")
    @Operation(summary = "Restore a soft-deleted department")
    public ResponseEntity<ApiResponse<DepartmentResponse>> restore(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(departmentService.restore(id)));
    }

    @PostMapping("/{id}/head")
    @Operation(summary = "Assign a new department head")
    public ResponseEntity<ApiResponse<DepartmentResponse>> assignHead(
            @PathVariable UUID id,
            @RequestParam UUID employeeId,
            @RequestParam String employeeName,
            @RequestParam String startDate) {
        AssignDepartmentHeadCommand command = new AssignDepartmentHeadCommand(
                id, employeeId, employeeName, LocalDate.parse(startDate));
        return ResponseEntity.ok(ApiResponse.success(departmentService.assignHead(command)));
    }
}
