package com.company.erp.modules.hr.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for creating a new Performance Review
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePerformanceReviewRequest {

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotBlank(message = "Employee name is required")
    @Size(max = 100, message = "Employee name must not exceed 100 characters")
    private String employeeName;

    private UUID departmentId;

    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String departmentName;

    @NotBlank(message = "Period is required")
    @Size(max = 50, message = "Period must not exceed 50 characters")
    private String period;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Double rating;

    @NotNull(message = "Objectives completed is required")
    @Min(value = 0, message = "Objectives completed cannot be negative")
    private Integer objectivesCompleted;

    @NotNull(message = "Total objectives is required")
    @Min(value = 1, message = "Total objectives must be at least 1")
    private Integer objectivesTotal;

    @Size(max = 2000, message = "Feedback must not exceed 2000 characters")
    private String feedback;

    private UUID reviewerId;

    @Size(max = 100, message = "Reviewer name must not exceed 100 characters")
    private String reviewerName;
}

