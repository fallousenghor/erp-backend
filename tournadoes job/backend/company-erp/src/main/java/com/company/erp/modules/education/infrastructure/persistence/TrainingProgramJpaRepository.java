package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingProgramJpaRepository extends JpaRepository<TrainingProgram, UUID> {

    Page<TrainingProgram> findByActive(boolean active, Pageable pageable);

    @Query("""
            SELECT p FROM TrainingProgram p
            LEFT JOIN FETCH p.modules m
            LEFT JOIN FETCH m.teacher
            WHERE p.id = :id
            """)
    Optional<TrainingProgram> findByIdWithModules(@Param("id") UUID id);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.program.id = :programId")
    long countEnrollments(@Param("programId") UUID programId);
}
