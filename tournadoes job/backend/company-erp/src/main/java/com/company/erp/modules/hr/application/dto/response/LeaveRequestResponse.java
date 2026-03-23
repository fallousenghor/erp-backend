package com.company.erp.modules.hr.application.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LeaveRequestResponse {
  UUID id;
  UUID employeeId;
  String employeeName;
  String departmentName;
  String leaveType;
  LocalDate startDate;
  LocalDate endDate;
  int days;
  String reason;
  String status;
  UUID approvedBy;
  LocalDateTime approvedAt;
}
