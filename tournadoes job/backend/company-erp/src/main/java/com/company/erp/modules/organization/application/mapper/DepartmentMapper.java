package com.company.erp.modules.organization.application.mapper;

import com.company.erp.modules.organization.domain.model.Department;
import com.company.erp.modules.organization.domain.model.DepartmentHead;
import com.company.erp.modules.organization.application.dto.response.DepartmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "currentHeadName", expression = "java(resolveHeadName(department))")
    @Mapping(target = "positionCount", expression = "java(department.getPositions().size())")
    DepartmentResponse toResponse(Department department);

    default String resolveHeadName(Department department) {
        DepartmentHead head = department.getCurrentHead();
        return head != null ? head.getEmployeeName() : null;
    }
}
