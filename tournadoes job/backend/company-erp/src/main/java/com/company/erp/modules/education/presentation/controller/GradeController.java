package com.company.erp.modules.education.presentation.controller;

import com.company.erp.modules.education.application.dto.request.CreateGradeRequest;
import com.company.erp.modules.education.application.dto.response.GradeStatsResponse;
import com.company.erp.modules.education.application.dto.response.StudentAverageResponse;
import com.company.erp.modules.education.application.service.GradeService;
import com.company.erp.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/grades")
@Tag(name = "Grades", description = "Education — Grade management and analytics")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @GetMapping("/stats")
    @Operation(summary = "Get grade statistics for program")
    @PreAuthorize("hasPermission(null, 'grade:read')")
    public ResponseEntity<ApiResponse<GradeStatsResponse>> getStats(
            @RequestParam(required = false) UUID programId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getStats(programId)));
    }

    @GetMapping("/students")
    @Operation(summary = "Get students with their averages for program")
    @PreAuthorize("hasPermission(null, 'grade:read')")
    public ResponseEntity<ApiResponse<List<StudentAverageResponse>>> getStudentsWithAverages(
            @RequestParam(required = false) UUID programId) {
        return ResponseEntity.ok(ApiResponse.success(gradeService.getStudentsWithAverages(programId)));
    }

    @PostMapping
    @Operation(summary = "Create grade")
    @PreAuthorize("hasPermission(null, 'grade:write')")
    public ResponseEntity<ApiResponse<Void>> createGrade(@Valid @RequestBody CreateGradeRequest request) {
        gradeService.createGrade(request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

