package com.company.erp.modules.attendance.infrastructure.persistence;

import com.company.erp.modules.attendance.domain.model.AttendanceRecord;
import com.company.erp.modules.attendance.domain.repository.AttendanceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository("attendanceJpaRepository")
public interface AttendanceJpaRepository extends AttendanceRepository, JpaRepository<AttendanceRecord, UUID> {
  // Native implementations will be added in domain/repository interface
}

