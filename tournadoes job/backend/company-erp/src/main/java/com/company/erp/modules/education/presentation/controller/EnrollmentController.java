package com.company.erp.modules.education.presentation.controller;

import com.company.erp.modules.education.application.dto.request.EnrollStudentRequest;
import com.company.erp.modules.education.application.dto.request.RecordGradeRequest;
import com.company.erp.modules.education.application.dto.response.EnrollmentResponse;
import com.company.erp.modules.education.application.service.EnrollmentService;
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

import java.util.UUID;

@RestController
@RequestMapping("/v1/enrollments")
@Tag(name = "Enrollments", description = "Education — Enrollment & grading")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @Operation(summary = "Enroll a student in a program")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(
            @Valid @RequestBody EnrollStudentRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(enrollmentService.enroll(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.findById(id)));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<PageResponse<EnrollmentResponse>>> findByStudent(
            @PathVariable UUID studentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                enrollmentService.findByStudent(studentId, PageRequest.of(page, size))));
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<ApiResponse<PageResponse<EnrollmentResponse>>> findByProgram(
            @PathVariable UUID programId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                enrollmentService.findByProgram(programId, PageRequest.of(page, size))));
    }

    @PostMapping("/{id}/grades")
    @Operation(summary = "Record a module grade for an enrollment")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> recordGrade(
            @PathVariable UUID id,
            @Valid @RequestBody RecordGradeRequest request) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.recordGrade(id, request)));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Mark an enrollment as completed and compute final average")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> complete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.complete(id)));
    }
}
