package com.company.erp.modules.attendance.application.dto.request;

import java.time.LocalTime;

public record UpdateAttendanceRequest(
  LocalTime checkInTime,
  LocalTime checkOutTime,
  String status, // PRESENT, ABSENT, LATE, ON_LEAVE, REMOTE
  String notes,
  String location
) {}
