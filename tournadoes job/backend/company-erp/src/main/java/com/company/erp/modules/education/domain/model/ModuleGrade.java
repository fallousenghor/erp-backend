package com.company.erp.modules.education.domain.model;

import com.company.erp.modules.education.domain.model.valueobject.Grade;
import com.company.erp.shared.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * ModuleGrade — grade obtained by a student for a specific CourseModule.
 * Owned by the Enrollment aggregate.
 */
@Entity
@Table(name = "module_grades",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_grade_enrollment_module",
                columnNames = {"enrollment_id", "module_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleGrade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @Embedded
    private Grade grade;

    @Column(name = "recorded_date", nullable = false)
    private LocalDate recordedDate;

    @Column(name = "comments", length = 500)
    private String comments;
}
