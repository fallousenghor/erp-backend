package com.company.erp.modules.education.domain.repository;

import com.company.erp.modules.education.domain.model.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TrainingProgramRepository {
    TrainingProgram save(TrainingProgram program);
    Optional<TrainingProgram> findById(UUID id);
    Page<TrainingProgram> findAll(Pageable pageable);
    Page<TrainingProgram> findByActive(boolean active, Pageable pageable);
    long countEnrollments(UUID programId);
}
