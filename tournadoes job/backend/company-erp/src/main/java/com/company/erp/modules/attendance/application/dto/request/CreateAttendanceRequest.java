package com.company.erp.modules.attendance.application.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record CreateAttendanceRequest(
  @NotNull UUID employeeId,
  @NotNull LocalDate recordDate,
  LocalTime checkInTime,
  LocalTime checkOutTime,
  @NotNull String status, // PRESENT, ABSENT, LATE, ON_LEAVE, REMOTE
  String notes,
  String location
) {}
