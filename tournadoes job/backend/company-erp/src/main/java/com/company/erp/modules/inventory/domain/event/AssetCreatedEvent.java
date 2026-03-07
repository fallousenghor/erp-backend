package com.company.erp.modules.inventory.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class AssetCreatedEvent extends BaseDomainEvent {
    private final UUID assetId;
    private final String assetCode;
    private final String name;

    public AssetCreatedEvent(UUID assetId, String assetCode, String name) {
        super("ASSET_CREATED");
        this.assetId = assetId;
        this.assetCode = assetCode;
        this.name = name;
    }
}
