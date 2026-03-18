package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.dto.request.CreatePerformanceReviewRequest;
import com.company.erp.modules.hr.application.dto.request.UpdatePerformanceReviewRequest;
import com.company.erp.modules.hr.application.service.PerformanceService;
import com.company.erp.modules.hr.domain.model.Objective;
import com.company.erp.modules.hr.domain.model.PerformanceReview;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Performance", description = "HR — Performance management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    // ==================== Performance Reviews - READ ====================

    @GetMapping("/performance-reviews")
    @Operation(summary = "Get all performance reviews (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<PerformanceReview>>> getReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PerformanceReview> reviewsPage = performanceService.getAllReviews(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(reviewsPage)));
    }

    @GetMapping("/performance-reviews/{id}")
    @Operation(summary = "Get performance review by ID")
    public ResponseEntity<ApiResponse<PerformanceReview>> getReview(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.getReviewById(id)));
    }

    @GetMapping("/performance-reviews/employee/{employeeId}")
    @Operation(summary = "Get performance reviews by employee ID")
    public ResponseEntity<ApiResponse<PageResponse<PerformanceReview>>> getReviewsByEmployee(
            @PathVariable UUID employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PerformanceReview> reviewsPage = performanceService.getReviewsByEmployee(employeeId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(reviewsPage)));
    }

    // ==================== Performance Reviews - CREATE ====================

    @PostMapping("/performance-reviews")
    @Operation(summary = "Create a performance review")
    public ResponseEntity<ApiResponse<PerformanceReview>> createReview(
            @Valid @RequestBody CreatePerformanceReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.createReview(request)));
    }

    // ==================== Performance Reviews - UPDATE ====================

    @PutMapping("/performance-reviews/{id}")
    @Operation(summary = "Update a performance review")
    public ResponseEntity<ApiResponse<PerformanceReview>> updateReview(
            @PathVariable UUID id, 
            @Valid @RequestBody UpdatePerformanceReviewRequest request) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.updateReview(id, request)));
    }

    @PatchMapping("/performance-reviews/{id}/status")
    @Operation(summary = "Update performance review status")
    public ResponseEntity<ApiResponse<PerformanceReview>> updateReviewStatus(
            @PathVariable UUID id, 
            @RequestBody Map<String, String> request) {
        PerformanceReview.ReviewStatus status = PerformanceReview.ReviewStatus.valueOf(request.get("status"));
        return ResponseEntity.ok(ApiResponse.success(performanceService.updateReviewStatus(id, status)));
    }

    // ==================== Performance Reviews - DELETE ====================

    @DeleteMapping("/performance-reviews/{id}")
    @Operation(summary = "Delete a performance review")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable UUID id) {
        performanceService.deleteReview(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ==================== Objectives - READ ====================

    @GetMapping("/objectives")
    @Operation(summary = "Get all objectives (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<Objective>>> getObjectives(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Objective> objectivesPage = performanceService.getAllObjectives(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(objectivesPage)));
    }

    @GetMapping("/objectives/{id}")
    @Operation(summary = "Get objective by ID")
    public ResponseEntity<ApiResponse<Objective>> getObjective(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.getObjectiveById(id)));
    }

    @GetMapping("/objectives/employee/{employeeId}")
    @Operation(summary = "Get objectives by employee ID")
    public ResponseEntity<ApiResponse<PageResponse<Objective>>> getObjectivesByEmployee(
            @PathVariable UUID employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Objective> objectivesPage = performanceService.getObjectivesByEmployee(employeeId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(objectivesPage)));
    }

    // ==================== Objectives - CREATE ====================

    @PostMapping("/objectives")
    @Operation(summary = "Create an objective")
    public ResponseEntity<ApiResponse<Objective>> createObjective(@RequestBody Objective objective) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.createObjective(objective)));
    }

    // ==================== Objectives - UPDATE ====================

    @PutMapping("/objectives/{id}")
    @Operation(summary = "Update an objective")
    public ResponseEntity<ApiResponse<Objective>> updateObjective(
            @PathVariable UUID id, 
            @RequestBody Objective objective) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.updateObjective(id, objective)));
    }

    @PatchMapping("/objectives/{id}/progress")
    @Operation(summary = "Update objective progress")
    public ResponseEntity<ApiResponse<Objective>> updateObjectiveProgress(
            @PathVariable UUID id, 
            @RequestBody Map<String, Integer> request) {
        int achieved = request.get("achieved");
        return ResponseEntity.ok(ApiResponse.success(performanceService.updateObjectiveProgress(id, achieved)));
    }

    @PatchMapping("/objectives/{id}/status")
    @Operation(summary = "Update objective status")
    public ResponseEntity<ApiResponse<Objective>> updateObjectiveStatus(
            @PathVariable UUID id, 
            @RequestBody Map<String, String> request) {
        Objective.ObjectiveStatus status = Objective.ObjectiveStatus.valueOf(request.get("status"));
        return ResponseEntity.ok(ApiResponse.success(performanceService.updateObjectiveStatus(id, status)));
    }

    // ==================== Objectives - DELETE ====================

    @DeleteMapping("/objectives/{id}")
    @Operation(summary = "Delete an objective")
    public ResponseEntity<ApiResponse<Void>> deleteObjective(@PathVariable UUID id) {
        performanceService.deleteObjective(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // ==================== Department Performance ====================

    @GetMapping("/performance/departments")
    @Operation(summary = "Get department performance statistics")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getDepartmentPerformance() {
        // Return mock department performance data
        List<Map<String, Object>> data = List.of(
            Map.of("departmentId", UUID.randomUUID().toString(), "departmentName", "Tech", "avgRating", 4.2, "employeeCount", 25),
            Map.of("departmentId", UUID.randomUUID().toString(), "departmentName", "RH", "avgRating", 3.8, "employeeCount", 8),
            Map.of("departmentId", UUID.randomUUID().toString(), "departmentName", "Finance", "avgRating", 4.5, "employeeCount", 12),
            Map.of("departmentId", UUID.randomUUID().toString(), "departmentName", "Ventes", "avgRating", 4.0, "employeeCount", 18),
            Map.of("departmentId", UUID.randomUUID().toString(), "departmentName", "Formation", "avgRating", 4.3, "employeeCount", 15)
        );
        return ResponseEntity.ok(ApiResponse.success(data));
    }
}

