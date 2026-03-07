package com.company.erp.modules.hr.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Performance Review Aggregate Root
 */
@Entity
@Table(name = "performance_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerformanceReview extends BaseAuditEntity {

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "employee_name", length = 100)
    private String employeeName;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "period", length = 50)
    private String period;

    @Column(name = "rating")
    private double rating;

    @Column(name = "objectives_completed")
    private int objectivesCompleted;

    @Column(name = "objectives_total")
    private int objectivesTotal;

    @Column(name = "feedback", length = 2000)
    private String feedback;

    @Column(name = "reviewer_id")
    private UUID reviewerId;

    @Column(name = "reviewer_name", length = 100)
    private String reviewerName;

    @Column(name = "reviewed_at")
    private LocalDate reviewedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ReviewStatus status = ReviewStatus.PENDING;

    public enum ReviewStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }
}

