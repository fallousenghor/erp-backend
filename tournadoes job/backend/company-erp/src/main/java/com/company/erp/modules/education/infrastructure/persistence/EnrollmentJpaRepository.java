package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentJpaRepository
        extends JpaRepository<Enrollment, UUID>, JpaSpecificationExecutor<Enrollment> {

    boolean existsByStudentIdAndProgramId(UUID studentId, UUID programId);
    Page<Enrollment> findByStudentId(UUID studentId, Pageable pageable);
    Page<Enrollment> findByProgramId(UUID programId, Pageable pageable);
    long countByProgramIdAndStatus(UUID programId, EnrollmentStatus status);
    long countByStatus(EnrollmentStatus status);

    @Query("""
            SELECT e FROM Enrollment e
            LEFT JOIN FETCH e.student
            LEFT JOIN FETCH e.program
            LEFT JOIN FETCH e.moduleGrades mg
            LEFT JOIN FETCH mg.module
            WHERE e.id = :id
            """)
    Optional<Enrollment> findByIdWithDetails(@Param("id") UUID id);
}
