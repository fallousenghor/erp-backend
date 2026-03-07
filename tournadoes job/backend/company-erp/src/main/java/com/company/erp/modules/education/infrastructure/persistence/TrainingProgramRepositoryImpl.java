package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.TrainingProgram;
import com.company.erp.modules.education.domain.repository.TrainingProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TrainingProgramRepositoryImpl implements TrainingProgramRepository {

    private final TrainingProgramJpaRepository jpaRepository;

    @Override public TrainingProgram save(TrainingProgram p)                  { return jpaRepository.save(p); }
    @Override public Page<TrainingProgram> findByActive(boolean a, Pageable pg) { return jpaRepository.findByActive(a, pg); }
    @Override public long countEnrollments(UUID id)                           { return jpaRepository.countEnrollments(id); }
    @Override public Page<TrainingProgram> findAll(Pageable pageable)         { return jpaRepository.findAll(pageable); }

    @Override
    public Optional<TrainingProgram> findById(UUID id) {
        return jpaRepository.findByIdWithModules(id);
    }
}
