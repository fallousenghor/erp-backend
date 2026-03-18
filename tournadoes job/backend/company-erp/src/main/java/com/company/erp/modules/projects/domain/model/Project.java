package com.company.erp.modules.projects.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Project Aggregate Root
 */
@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project extends BaseAuditEntity {

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "client_name", length = 200)
    private String clientName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.PLANNING;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", length = 20)
    @Builder.Default
    private ProjectPriority priority = ProjectPriority.MEDIUM;

    @Column(name = "progress")
    @Builder.Default
    private Integer progress = 0;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "manager_id")
    private UUID managerId;

    @Column(name = "manager_name", length = 100)
    private String managerName;

    @Column(name = "deleted")
    @Builder.Default
    private boolean deleted = false;

    public enum ProjectStatus {
        PLANNING,
        ACTIVE,
        ON_HOLD,
        COMPLETED,
        CANCELLED
    }

    public enum ProjectPriority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}

