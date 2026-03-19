package com.company.erp.modules.education.domain.repository;

import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository {
    Enrollment save(Enrollment enrollment);

    Optional<Enrollment> findById(UUID id);
    boolean existsByStudentIdAndProgramId(UUID studentId, UUID programId);
    Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable);
    Page<Enrollment> findByProgramId(UUID programId, Pageable pageable);
    Page<Enrollment> findAll(Specification<Enrollment> spec, Pageable pageable);
    long countByProgramIdAndStatus(UUID programId, EnrollmentStatus status);

    // Grade analytics
    List<StudentAverageProjection> findStudentAveragesByProgramId(UUID programId);
    GradeStatsProjection computeStatsByProgramId(UUID programId);

    interface StudentAverageProjection {
        UUID getStudentId();
        String getFirstName();
        String getLastName();
        String getEmail();
        String getProgramTitle();
        String getAvatarUrl();
        BigDecimal getAverage();
        Long getGradesCount();
    }

    interface GradeStatsProjection {
        BigDecimal getAverage();
        BigDecimal getHighest();
        BigDecimal getLowest();
        BigDecimal getPassRate();
        Long getTotalStudents();
    }
}
