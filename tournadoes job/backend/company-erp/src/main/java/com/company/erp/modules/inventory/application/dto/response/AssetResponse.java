package com.company.erp.modules.inventory.application.dto.response;

import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetCondition;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AssetResponse(
        UUID id,
        String assetCode,
        String name,
        String description,
        Asset.AssetCategory category,
        AssetStatus status,
        AssetCondition conditionState,
        LocalDate purchaseDate,
        BigDecimal purchasePrice,
        String serialNumber,
        String brand,
        String model,
        String location,
        UUID departmentId,
        String imageUrl,
        String documentUrl,
        String assignedToName,
        LocalDateTime createdAt
) {}
