package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.Attendance;
import com.company.erp.modules.hr.domain.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository jpaRepository;

    @Override public Attendance save(Attendance a)                              { return jpaRepository.save(a); }
    
    @Override public Optional<Attendance> findById(UUID id)                   { return jpaRepository.findById(id); }
    
    @Override public boolean existsByEmployeeIdAndDate(UUID id, LocalDate d)   { return jpaRepository.existsByEmployeeIdAndAttendanceDate(id, d); }

    @Override
    public Optional<Attendance> findByEmployeeIdAndDate(UUID employeeId, LocalDate date) {
        return jpaRepository.findByEmployeeIdAndAttendanceDate(employeeId, date);
    }

    @Override
    public List<Attendance> findByEmployeeIdAndDateRange(UUID employeeId, LocalDate from, LocalDate to) {
        return jpaRepository.findByEmployeeIdAndDateRange(employeeId, from, to);
    }
    
    @Override
    public List<Attendance> findAll() {
        return jpaRepository.findAll();
    }
}
