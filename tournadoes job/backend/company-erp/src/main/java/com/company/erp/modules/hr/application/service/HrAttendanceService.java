package com.company.erp.modules.hr.application.service;

import com.company.erp.modules.hr.application.dto.response.AttendanceResponse;
import com.company.erp.modules.hr.domain.model.Attendance;
import com.company.erp.modules.hr.domain.repository.AttendanceRepository;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HrAttendanceService {

    private final AttendanceRepository attendanceRepository;

    @PreAuthorize("hasPermission(null, 'employee:update')")
    public AttendanceResponse recordAttendance(UUID employeeId, LocalDate date,
                                                LocalTime checkIn, LocalTime checkOut,
                                                Attendance.AttendanceStatus status,
                                                String notes) {
        // Check if attendance already exists for this employee and date
        var existingAttendance = attendanceRepository.findByEmployeeIdAndDate(employeeId, date);
        
        if (existingAttendance.isPresent()) {
            // Update existing attendance instead of throwing error
            Attendance attendance = existingAttendance.get();
            attendance.setCheckIn(checkIn);
            attendance.setCheckOut(checkOut);
            attendance.setStatus(status != null ? status : Attendance.AttendanceStatus.PRESENT);
            attendance.setNotes(notes);
            return toResponse(attendanceRepository.save(attendance));
        }

        Attendance attendance = Attendance.builder()
                .employeeId(employeeId)
                .attendanceDate(date)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .status(status != null ? status : Attendance.AttendanceStatus.PRESENT)
                .notes(notes)
                .build();

        return toResponse(attendanceRepository.save(attendance));
    }

    @PreAuthorize("hasPermission(null, 'employee:update')")
    public AttendanceResponse updateAttendance(UUID attendanceId,
                                                LocalTime checkIn, LocalTime checkOut,
                                                Attendance.AttendanceStatus status,
                                                String notes) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Attendance not found with id: " + attendanceId));
        
        if (checkIn != null) attendance.setCheckIn(checkIn);
        if (checkOut != null) attendance.setCheckOut(checkOut);
        if (status != null) attendance.setStatus(status);
        if (notes != null) attendance.setNotes(notes);
        
        return toResponse(attendanceRepository.save(attendance));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'employee:read')")
    public List<AttendanceResponse> findByEmployeeAndRange(UUID employeeId,
                                                            LocalDate from, LocalDate to) {
        return attendanceRepository.findByEmployeeIdAndDateRange(employeeId, from, to)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'employee:read')")
    public AttendanceResponse findById(UUID attendanceId) {
        return attendanceRepository.findById(attendanceId)
                .map(this::toResponse)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, 
                        "Attendance not found with id: " + attendanceId));
    }

    private AttendanceResponse toResponse(Attendance a) {
        return new AttendanceResponse(a.getId(), a.getEmployeeId(), a.getAttendanceDate(),
                a.getCheckIn(), a.getCheckOut(), a.getStatus(), a.getNotes());
    }
}
