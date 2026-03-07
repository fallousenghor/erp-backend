package com.company.erp.modules.hr.presentation.controller;

import com.company.erp.modules.hr.application.service.PerformanceService;
import com.company.erp.modules.hr.domain.model.Objective;
import com.company.erp.modules.hr.domain.model.PerformanceReview;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/v1")
@Tag(name = "Performance", description = "HR — Performance management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    // ==================== Performance Reviews ====================

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

    @PostMapping("/performance-reviews")
    @Operation(summary = "Create a performance review")
    public ResponseEntity<ApiResponse<PerformanceReview>> createReview(@RequestBody PerformanceReview review) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.createReview(review)));
    }

    // ==================== Objectives ====================

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

    @PostMapping("/objectives")
    @Operation(summary = "Create an objective")
    public ResponseEntity<ApiResponse<Objective>> createObjective(@RequestBody Objective objective) {
        return ResponseEntity.ok(ApiResponse.success(performanceService.createObjective(objective)));
    }

    @PatchMapping("/objectives/{id}/progress")
    @Operation(summary = "Update objective progress")
    public ResponseEntity<ApiResponse<Objective>> updateObjectiveProgress(
            @PathVariable UUID id, 
            @RequestBody Map<String, Integer> request) {
        int achieved = request.get("achieved");
        return ResponseEntity.ok(ApiResponse.success(performanceService.updateObjectiveProgress(id, achieved)));
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

