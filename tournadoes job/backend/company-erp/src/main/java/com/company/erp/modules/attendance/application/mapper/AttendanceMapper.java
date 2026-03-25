package com.company.erp.modules.attendance.application.mapper;

import com.company.erp.modules.attendance.application.dto.response.AttendanceResponse;
import com.company.erp.modules.attendance.domain.model.AttendanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

// @Mapping(target = "id", source = "id") // id auto-generated
  @Mapping(target = "employeeId", source = "employeeId")
  @Mapping(target = "employeeNumber", source = "employeeNumber")
  @Mapping(target = "employeeName", source = "employeeName")
  @Mapping(target = "departmentId", source = "departmentId")
  @Mapping(target = "departmentName", source = "departmentName")
  @Mapping(target = "recordDate", source = "recordDate")
  @Mapping(target = "checkInTime", source = "checkInTime")
  @Mapping(target = "checkOutTime", source = "checkOutTime")
  @Mapping(target = "workedHours", source = "workedHours")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "lateMinutes", source = "lateMinutes")
  @Mapping(target = "notes", source = "notes")
  @Mapping(target = "location", source = "location")
  AttendanceResponse toResponse(AttendanceRecord record);
}

