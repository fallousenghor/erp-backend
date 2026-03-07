package com.company.erp.modules.inventory.application.dto.request;

import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateAssetRequest(
        @NotBlank String name,
        String description,
        @NotNull Asset.AssetCategory category,
        AssetCondition conditionState,
        LocalDate purchaseDate,
        BigDecimal purchasePrice,
        String serialNumber,
        String brand,
        String model,
        String location,
        UUID departmentId
) {}
