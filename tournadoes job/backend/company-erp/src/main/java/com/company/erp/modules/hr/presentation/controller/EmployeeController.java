package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.request.CreateEmployeeRequest;
import com.company.erp.modules.hr.application.dto.request.TerminateEmployeeRequest;
import com.company.erp.modules.hr.application.dto.request.UpdateEmployeeRequest;
import com.company.erp.modules.hr.application.dto.response.EmployeeResponse;
import com.company.erp.modules.hr.application.service.EmployeeService;
import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import com.company.erp.shared.response.PageResponse;
import com.company.erp.shared.service.ImageUploadService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@Slf4j
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ImageUploadService imageUploadService;

    @GetMapping
    public ResponseEntity<PageResponse<EmployeeResponse>> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) UUID departmentId,
            Pageable pageable) {
        return ResponseEntity.ok(employeeService.findAll(name, status, departmentId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @PreAuthorize("hasPermission(null, 'employee:create')")
    @PostMapping("/with-photo")
    public ResponseEntity<EmployeeResponse> createEmployeeWithPhoto(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("email") String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "hireDate", required = false) String hireDateStr,
            @RequestParam(value = "positionTitle", required = false) String positionTitle,
            @RequestParam(value = "baseSalary", required = false) String baseSalaryStr,
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam("contractType") String contractType,
            @RequestParam(value = "contractStartDate", required = false) String contractStartDateStr,
            @RequestParam(value = "departmentId", required = false) String departmentIdStr,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        try {
            // Parse inputs
            LocalDate hireDate = hireDateStr != null ? LocalDate.parse(hireDateStr) : LocalDate.now();
            LocalDate contractStartDate = contractStartDateStr != null ? LocalDate.parse(contractStartDateStr) : hireDate;
            BigDecimal baseSalary = baseSalaryStr != null ? new BigDecimal(baseSalaryStr) : BigDecimal.ZERO;
            UUID departmentId = null;
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    departmentId = UUID.fromString(departmentIdStr);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid departmentId: {}", departmentIdStr);
                }
            }

            // TEMP: Generate employeeNumber before create (service generates inside)
            String tempEmployeeNumber = "EMP-" + LocalDate.now().getYear() + "-" + String.format("%05d", (int) (Math.random() * 99999));

            // Upload photo to Cloudinary if provided
            String photoUrl = null;
            if (photo != null && !photo.isEmpty()) {
                photoUrl = imageUploadService.uploadImage(photo, "hr/employees/" + tempEmployeeNumber);
            }

            // Create request
            CreateEmployeeRequest request = new CreateEmployeeRequest(
                    firstName.trim(),
                    lastName.trim(),
                    email.trim().toLowerCase(),
                    phone != null ? phone.trim() : null,
                    null,  // birthDate not in form
                    hireDate,
                    departmentId,
                    null,  // departmentName not sent
                    null,  // positionId not in form
                    positionTitle != null ? positionTitle.trim() : null,
                    baseSalary,
                    currency != null ? currency : "XOF",
                    Contract.ContractType.valueOf(contractType.toUpperCase()),
                    contractStartDate,
                    null,  // contractEndDate not in form
                    photoUrl
            );

            EmployeeResponse response = employeeService.create(request);
            log.info("Employee created with photo: {}", response.employeeNumber());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error creating employee with photo", e);
            throw new RuntimeException("Failed to create employee: " + e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(null, 'employee:update')")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        EmployeeResponse response = employeeService.update(id, request);
        log.info("Employee updated: {}", response.employeeNumber());
        return ResponseEntity.ok(response);
    }

@PreAuthorize("hasPermission(null, 'employee:update')")
    @PutMapping("/{id}/photo")
    public ResponseEntity<EmployeeResponse> updateEmployeePhoto(
            @PathVariable UUID id,
            @RequestParam("photo") MultipartFile photo) {
        try {
            String employeeNumber = employeeService.findById(id).employeeNumber();
            String photoUrl = imageUploadService.uploadImage(photo, "hr/employees/" + employeeNumber);
            EmployeeResponse response = employeeService.updatePhoto(id, photoUrl);
            log.info("Employee photo updated: {}", employeeNumber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error updating employee photo", e);
            throw new RuntimeException("Failed to update photo: " + e.getMessage(), e);
        }
    }

    @PreAuthorize("hasPermission(null, 'employee:update')")
    @PostMapping("/{id}/terminate")
    public ResponseEntity<EmployeeResponse> terminateEmployee(
            @PathVariable UUID id,
            @Valid @RequestBody TerminateEmployeeRequest request) {
        EmployeeResponse response = employeeService.terminate(id, request.terminationDate());
        log.info("Employee terminated: id={} date={} reason={}", 
                id, request.terminationDate(), request.reason());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(null, 'employee:delete')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.delete(id);
        log.info("Employee hard deleted: {}", id);
        return ResponseEntity.noContent().build();
    }
}
