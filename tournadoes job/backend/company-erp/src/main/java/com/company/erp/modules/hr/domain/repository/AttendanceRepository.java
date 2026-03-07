package com.company.erp.modules.hr.domain.repository;

import com.company.erp.modules.hr.domain.model.Attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttendanceRepository {

    Attendance save(Attendance attendance);

    Optional<Attendance> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);

    boolean existsByEmployeeIdAndDate(UUID employeeId, LocalDate date);

    List<Attendance> findByEmployeeIdAndDateRange(UUID employeeId, LocalDate from, LocalDate to);
}
