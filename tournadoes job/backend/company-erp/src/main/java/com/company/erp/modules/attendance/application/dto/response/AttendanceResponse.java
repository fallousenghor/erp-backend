package com.company.erp.modules.attendance.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

  UUID id;

  UUID employeeId;

  String employeeNumber;

  String employeeName;

  UUID departmentId;

  String departmentName;

  LocalDate recordDate;

  LocalTime checkInTime;

  LocalTime checkOutTime;

  String workedHours; // Stored as String "HH:mm"

  String status;

  Integer lateMinutes;

  String notes;

  String location;

  String ipAddress;

  String deviceInfo;
}

