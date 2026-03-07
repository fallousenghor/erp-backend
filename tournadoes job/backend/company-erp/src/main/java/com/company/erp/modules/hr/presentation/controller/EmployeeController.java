package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.request.CreateEmployeeRequest;
import com.company.erp.modules.hr.application.dto.request.CreateEmployeeRequestWithPhoto;
import com.company.erp.modules.hr.application.dto.response.EmployeeResponse;
import com.company.erp.modules.hr.application.service.EmployeeService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import com.company.erp.shared.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/v1/employees")
@Tag(name = "Employees", description = "HR — Employee management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ImageUploadService imageUploadService;

    @PostMapping
    @Operation(summary = "Create a new employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> create(
            @Valid @RequestBody CreateEmployeeRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(employeeService.create(request)));
    }

    @PostMapping(value = "/with-photo", consumes = {"multipart/form-data"})
    @Operation(summary = "Create a new employee with photo")
    public ResponseEntity<ApiResponse<EmployeeResponse>> createWithPhoto(
            @ModelAttribute CreateEmployeeRequestWithPhoto request) {
        
        // Upload photo to Cloudinary if provided
        String photoUrl = null;
        if (request.photo() != null && !request.photo().isEmpty()) {
            photoUrl = imageUploadService.uploadImage(request.photo(), "employees");
        }
        
        // Create employee with photo URL
        CreateEmployeeRequest employeeRequest = new CreateEmployeeRequest(
                request.firstName(),
                request.lastName(),
                request.email(),
                request.phone(),
                request.birthDate(),
                request.hireDate(),
                request.departmentId(),
                request.departmentName(),
                request.positionId(),
                request.positionTitle(),
                request.baseSalary(),
                request.currency(),
                request.contractType(),
                request.contractStartDate(),
                request.contractEndDate(),
                photoUrl
        );
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(employeeService.create(employeeRequest)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID")
    public ResponseEntity<ApiResponse<EmployeeResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List employees with filtering and pagination")
    public ResponseEntity<ApiResponse<PageResponse<EmployeeResponse>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                employeeService.findAll(name, status, departmentId,
                        PageRequest.of(page, size, Sort.by("lastName")))));
    }

    @PostMapping("/{id}/terminate")
    @Operation(summary = "Terminate an employee")
    public ResponseEntity<ApiResponse<EmployeeResponse>> terminate(
            @PathVariable UUID id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate terminationDate) {
        return ResponseEntity.ok(ApiResponse.success(
                employeeService.terminate(id, terminationDate)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee information")
    public ResponseEntity<ApiResponse<EmployeeResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody com.company.erp.modules.hr.application.dto.request.UpdateEmployeeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(employeeService.update(id, request)));
    }

    @PutMapping(value = "/{id}/photo", consumes = {"multipart/form-data"})
    @Operation(summary = "Update employee photo")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updatePhoto(
            @PathVariable UUID id,
            @RequestParam("photo") MultipartFile photo) {
        
        String photoUrl = null;
        if (photo != null && !photo.isEmpty()) {
            photoUrl = imageUploadService.uploadImage(photo, "employees");
        }
        
        return ResponseEntity.ok(ApiResponse.success(employeeService.updatePhoto(id, photoUrl)));
    }
}

