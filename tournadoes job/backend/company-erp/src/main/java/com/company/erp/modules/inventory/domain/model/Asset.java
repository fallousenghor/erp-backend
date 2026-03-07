package com.company.erp.modules.inventory.domain.model;

import com.company.erp.modules.inventory.domain.model.valueobject.AssetCondition;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import com.company.erp.shared.base.BaseAuditEntity;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Asset Aggregate Root.
 */
@Entity
@Table(name = "assets",
        uniqueConstraints = @UniqueConstraint(name = "uk_asset_code", columnNames = "asset_code"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset extends BaseAuditEntity {

    public enum AssetCategory {
        COMPUTER, VEHICLE, FURNITURE, EQUIPMENT,
        PHONE, PRINTER, SERVER, OTHER
    }

    @Column(name = "asset_code", nullable = false, unique = true, length = 20)
    private String assetCode;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private AssetCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @Builder.Default
    private AssetStatus status = AssetStatus.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_state", nullable = false, length = 20)
    @Builder.Default
    private AssetCondition conditionState = AssetCondition.NEW;

    @Column(name = "purchase_date")
    private LocalDate purchaseDate;

    @Column(name = "purchase_price", precision = 15, scale = 2)
    private BigDecimal purchasePrice;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "document_url", length = 500)
    private String documentUrl;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AssetAssignment> assignments = new ArrayList<>();

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void assignTo(UUID employeeId, String employeeName, LocalDate assignedDate, String notes) {
        if (this.status != AssetStatus.AVAILABLE) {
            throw new BusinessException(ErrorCode.ASSET_ALREADY_ASSIGNED,
                    "Asset " + this.assetCode + " is not available (status: " + this.status + ")");
        }
        AssetAssignment assignment = AssetAssignment.builder()
                .asset(this)
                .employeeId(employeeId)
                .employeeName(employeeName)
                .assignedDate(assignedDate)
                .notes(notes)
                .active(true)
                .build();
        this.assignments.add(assignment);
        this.status = AssetStatus.ASSIGNED;
    }

    public void returnAsset(LocalDate returnDate) {
        AssetAssignment active = getActiveAssignment();
        if (active == null) {
            throw new BusinessException(ErrorCode.ASSET_NOT_ASSIGNED,
                    "Asset " + this.assetCode + " has no active assignment");
        }
        active.returnAsset(returnDate);
        this.status = AssetStatus.AVAILABLE;
    }

    public AssetAssignment getActiveAssignment() {
        return this.assignments.stream()
                .filter(AssetAssignment::isActive)
                .findFirst()
                .orElse(null);
    }

    public void decommission() {
        if (this.status == AssetStatus.ASSIGNED) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    "Cannot decommission an assigned asset. Return it first.");
        }
        this.status = AssetStatus.DECOMMISSIONED;
    }
}
