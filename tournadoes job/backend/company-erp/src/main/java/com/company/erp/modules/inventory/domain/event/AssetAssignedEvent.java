package com.company.erp.modules.inventory.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class AssetAssignedEvent extends BaseDomainEvent {
    private final UUID assetId;
    private final String assetCode;
    private final UUID employeeId;
    private final String employeeName;

    public AssetAssignedEvent(UUID assetId, String assetCode, UUID employeeId, String employeeName) {
        super("ASSET_ASSIGNED");
        this.assetId = assetId;
        this.assetCode = assetCode;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
    }
}
