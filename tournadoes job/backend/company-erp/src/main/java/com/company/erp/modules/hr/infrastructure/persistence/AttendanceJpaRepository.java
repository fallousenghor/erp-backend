package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<Attendance, UUID> {

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(UUID employeeId, LocalDate date);

    boolean existsByEmployeeIdAndAttendanceDate(UUID employeeId, LocalDate date);

    @Query("""
            SELECT a FROM Attendance a
            WHERE a.employeeId = :employeeId
            AND a.attendanceDate BETWEEN :from AND :to
            ORDER BY a.attendanceDate
            """)
    List<Attendance> findByEmployeeIdAndDateRange(
            @Param("employeeId") UUID employeeId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to);
}
