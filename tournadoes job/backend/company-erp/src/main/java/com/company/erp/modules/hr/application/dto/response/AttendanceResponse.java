package com.company.erp.modules.hr.application.dto.response;

import com.company.erp.modules.hr.domain.model.Attendance;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record AttendanceResponse(
        UUID id,
        UUID employeeId,
        String employeeNumber,
        String employeeName,
        String departmentName,
        LocalDate attendanceDate,
        LocalTime checkIn,
        LocalTime checkOut,
        Attendance.AttendanceStatus status,
        String notes
) {}
