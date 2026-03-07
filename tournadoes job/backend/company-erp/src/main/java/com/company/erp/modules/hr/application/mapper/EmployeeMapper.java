package com.company.erp.modules.hr.application.mapper;

import com.company.erp.modules.hr.domain.model.Employee;
import com.company.erp.modules.hr.application.dto.response.EmployeeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "baseSalary",        source = "salary.baseSalary")
    @Mapping(target = "currency",          source = "salary.currency")
    @Mapping(target = "contractType",      source = "contract.contractType")
    @Mapping(target = "contractStartDate", source = "contract.startDate")
    @Mapping(target = "contractEndDate",   source = "contract.endDate")
    EmployeeResponse toResponse(Employee employee);
}
