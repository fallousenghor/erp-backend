package com.company.erp.modules.education.domain.model;

import com.company.erp.modules.education.domain.model.valueobject.ProgramLevel;
import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TrainingProgram Aggregate Root.
 * Owns CourseModules. Enrollments are separate aggregates.
 */
@Entity
@Table(name = "training_programs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainingProgram extends BaseAuditEntity {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 20)
    private ProgramLevel level;

    @Column(name = "duration_weeks", nullable = false)
    private int durationWeeks;

    @Column(name = "max_students")
    private Integer maxStudents;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "passing_score", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private java.math.BigDecimal passingScore = new java.math.BigDecimal("10.00");

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private List<CourseModule> modules = new ArrayList<>();

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void addModule(String title, String description,
                           int durationHours, double coefficient, Teacher teacher) {
        CourseModule module = CourseModule.builder()
                .title(title)
                .description(description)
                .durationHours(durationHours)
                .coefficient(coefficient)
                .orderIndex(modules.size() + 1)
                .program(this)
                .teacher(teacher)
                .build();
        this.modules.add(module);
    }

    public int getTotalHours() {
        return modules.stream().mapToInt(CourseModule::getDurationHours).sum();
    }

    public boolean isFull(long currentEnrollmentCount) {
        return maxStudents != null && currentEnrollmentCount >= maxStudents;
    }
}
