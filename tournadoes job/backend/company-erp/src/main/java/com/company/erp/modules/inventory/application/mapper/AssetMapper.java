package com.company.erp.modules.inventory.application.mapper;

import com.company.erp.modules.inventory.application.dto.response.AssetResponse;
import com.company.erp.modules.inventory.domain.model.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "assignedToName",
             expression = "java(asset.getActiveAssignment() != null ? asset.getActiveAssignment().getEmployeeName() : null)")
    AssetResponse toResponse(Asset asset);
}
