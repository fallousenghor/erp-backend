package com.company.erp.modules.hr.application.mapper;

import com.company.erp.modules.hr.application.dto.response.LeaveRequestResponse;
import com.company.erp.modules.hr.domain.model.LeaveRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

  LeaveRequestResponse toResponse(LeaveRequest leave);

}
