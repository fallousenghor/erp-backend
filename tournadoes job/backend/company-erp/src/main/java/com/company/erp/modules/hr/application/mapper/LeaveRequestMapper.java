package com.company.erp.modules.hr.application.mapper;

import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.application.dto.response.LeaveRequestResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    LeaveRequestResponse toResponse(LeaveRequest leaveRequest);
}
