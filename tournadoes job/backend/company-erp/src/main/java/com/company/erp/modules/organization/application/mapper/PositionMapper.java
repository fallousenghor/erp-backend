package com.company.erp.modules.organization.application.mapper;

import com.company.erp.modules.organization.domain.model.Position;
import com.company.erp.modules.organization.application.dto.response.PositionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    PositionResponse toResponse(Position position);
}
