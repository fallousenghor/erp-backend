package com.company.erp.modules.education.presentation.controller;

import com.company.erp.modules.education.application.dto.request.CreateStudentRequest;
import com.company.erp.modules.education.application.dto.response.StudentResponse;
import com.company.erp.modules.education.application.service.StudentService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/students")
@Tag(name = "Students", description = "Education — Student management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @Operation(summary = "Register a new student")
    public ResponseEntity<ApiResponse<StudentResponse>> create(
            @Valid @RequestBody CreateStudentRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(studentService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(studentService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<StudentResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                studentService.findAll(PageRequest.of(page, size, Sort.by("lastName")))));
    }
}
