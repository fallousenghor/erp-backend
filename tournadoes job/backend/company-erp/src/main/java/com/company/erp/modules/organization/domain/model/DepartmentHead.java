package com.company.erp.modules.organization.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * DepartmentHead entity — tracks who heads a department and for which period.
 * Allows historical tracking of department leadership changes.
 */
@Entity
@Table(name = "department_heads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentHead extends BaseAuditEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    /**
     * Reference to the Employee aggregate (cross-module ref by ID).
     */
    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(name = "employee_name", nullable = false, length = 160)
    private String employeeName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "current", nullable = false)
    @Builder.Default
    private boolean current = true;

    public void end(LocalDate endDate) {
        this.endDate = endDate;
        this.current = false;
    }
}
