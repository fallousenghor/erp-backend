package com.company.erp.modules.education.domain.model;

import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import com.company.erp.modules.education.domain.model.valueobject.Grade;
import com.company.erp.shared.base.BaseAuditEntity;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Enrollment Aggregate Root — links a Student to a TrainingProgram.
 * Manages grades per module and final average calculation.
 */
@Entity
@Table(name = "enrollments",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_enrollment_student_program",
                columnNames = {"student_id", "program_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private TrainingProgram program;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ACTIVE;

    @Column(name = "final_average", precision = 5, scale = 2)
    private BigDecimal finalAverage;

    @Column(name = "final_letter_grade", length = 2)
    private String finalLetterGrade;

    @Column(name = "passed", nullable = false)
    @Builder.Default
    private boolean passed = false;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ModuleGrade> moduleGrades = new ArrayList<>();

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void recordGrade(CourseModule module, BigDecimal score, BigDecimal maxScore) {
        if (this.status != EnrollmentStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR,
                    "Cannot record grade for non-active enrollment");
        }
        // Remove existing grade for this module if any
        moduleGrades.removeIf(mg -> mg.getModule().getId().equals(module.getId()));

        ModuleGrade moduleGrade = ModuleGrade.builder()
                .enrollment(this)
                .module(module)
                .grade(Grade.of(score, maxScore))
                .recordedDate(LocalDate.now())
                .build();
        moduleGrades.add(moduleGrade);
        recalculateAverage();
    }

    public void complete() {
        if (this.status != EnrollmentStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.ENROLLMENT_ALREADY_COMPLETED);
        }
        recalculateAverage();
        this.status = EnrollmentStatus.COMPLETED;
        this.completionDate = LocalDate.now();
        this.passed = finalAverage != null && finalAverage.compareTo(new BigDecimal("10")) >= 0;
    }

    public void drop() {
        this.status = EnrollmentStatus.DROPPED;
    }

    private void recalculateAverage() {
        if (moduleGrades.isEmpty()) return;

        double totalWeight = moduleGrades.stream()
                .mapToDouble(mg -> mg.getModule().getCoefficient())
                .sum();

        if (totalWeight == 0) return;

        double weightedSum = moduleGrades.stream()
                .mapToDouble(mg ->
                        mg.getGrade().outOf20().doubleValue() * mg.getModule().getCoefficient())
                .sum();

        this.finalAverage = BigDecimal.valueOf(weightedSum / totalWeight)
                .setScale(2, RoundingMode.HALF_UP);

        this.finalLetterGrade = Grade.of(
                this.finalAverage,
                new BigDecimal("20")).letterGrade();
    }
}
