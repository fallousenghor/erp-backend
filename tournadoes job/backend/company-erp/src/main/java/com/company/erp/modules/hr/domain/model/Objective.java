package com.company.erp.modules.hr.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Objective Aggregate Root
 */
@Entity
@Table(name = "objectives")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Objective extends BaseAuditEntity {

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "employee_name", length = 100)
    private String employeeName;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "target")
    private int target;

    @Column(name = "achieved")
    private int achieved;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ObjectiveStatus status = ObjectiveStatus.PENDING;

    @Column(name = "due_date")
    private LocalDate dueDate;

    public enum ObjectiveStatus {
        PENDING,
        ACHIEVED,
        EXCEEDED,
        AT_RISK
    }
}

