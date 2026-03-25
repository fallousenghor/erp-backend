package com.company.erp.modules.organization.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Department Aggregate Root.
 * Owns Positions and DepartmentHeads.
 * Supports soft delete and head assignment history.
 */
@Entity
@Table(name = "departments",
        uniqueConstraints = @UniqueConstraint(name = "uk_department_code", columnNames = "code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department extends BaseAuditEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    private String code;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private java.time.LocalDateTime deletedAt;

    @Column(name = "budget", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private java.math.BigDecimal budget = java.math.BigDecimal.ZERO;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Position> positions = new ArrayList<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DepartmentHead> heads = new ArrayList<>();

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void addPosition(Position position) {
        position.setDepartment(this);
        this.positions.add(position);
    }

    public void assignHead(UUID employeeId, String employeeName, LocalDate startDate) {
        // End the current head assignment
        this.heads.stream()
                .filter(DepartmentHead::isCurrent)
                .forEach(h -> h.end(startDate.minusDays(1)));

        DepartmentHead newHead = DepartmentHead.builder()
                .department(this)
                .employeeId(employeeId)
                .employeeName(employeeName)
                .startDate(startDate)
                .current(true)
                .build();

        this.heads.add(newHead);
    }

    public DepartmentHead getCurrentHead() {
        return this.heads.stream()
                .filter(DepartmentHead::isCurrent)
                .findFirst()
                .orElse(null);
    }

    public void softDelete() {
        this.deleted = true;
        this.active = false;
        this.deletedAt = java.time.LocalDateTime.now();
    }

    // ── Budget domain logic ──────────────────────────────────────────────────

    public void updateBudget(BigDecimal newBudget) {
        if (newBudget == null || newBudget.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Budget must be non-negative");
        }
        this.budget = newBudget;
    }

    public BigDecimal remainingBudget() {
        // To be computed from expenses - placeholder
        return this.budget;
    }
}
