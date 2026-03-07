package com.company.erp.modules.inventory.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class AssetReturnedEvent extends BaseDomainEvent {
    private final UUID assetId;
    private final String assetCode;

    public AssetReturnedEvent(UUID assetId, String assetCode) {
        super("ASSET_RETURNED");
        this.assetId = assetId;
        this.assetCode = assetCode;
    }
}
