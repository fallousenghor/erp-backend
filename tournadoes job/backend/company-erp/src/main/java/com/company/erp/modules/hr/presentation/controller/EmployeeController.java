package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.response.EmployeeResponse;
import com.company.erp.modules.hr.application.service.EmployeeService;
import com.company.erp.shared.response.PageResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hr/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

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

    // TODO: POST/PUT/DELETE via EmployeeService
}

