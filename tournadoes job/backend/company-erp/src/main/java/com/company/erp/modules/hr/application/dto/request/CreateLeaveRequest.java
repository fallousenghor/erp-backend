package com.company.erp.modules.hr.application.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLeaveRequest {

  @NotNull(message = "Employee ID is required")
  private java.util.UUID employeeId;

  @NotNull(message = "Leave type is required")
  private String leaveType;

  @NotNull(message = "Start date is required")
  @Future(message = "Start date must be in the future")
  private LocalDate startDate;

  @NotNull(message = "End date is required")
  @Future(message = "End date must be in the future")
  private LocalDate endDate;

  @NotBlank(message = "Reason is required")
  private String reason;

}
