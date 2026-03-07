package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import com.company.erp.modules.education.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EnrollmentRepositoryImpl implements EnrollmentRepository {

    private final EnrollmentJpaRepository jpaRepository;

    @Override public Enrollment save(Enrollment e)                             { return jpaRepository.save(e); }
    @Override public boolean existsByStudentIdAndProgramId(UUID s, UUID p)    { return jpaRepository.existsByStudentIdAndProgramId(s, p); }
    @Override public Page<Enrollment> findByStudentId(UUID id, Pageable pg)   { return jpaRepository.findByStudentId(id, pg); }
    @Override public Page<Enrollment> findByProgramId(UUID id, Pageable pg)   { return jpaRepository.findByProgramId(id, pg); }
    @Override public long countByProgramIdAndStatus(UUID id, EnrollmentStatus st) { return jpaRepository.countByProgramIdAndStatus(id, st); }

    @Override
    public Optional<Enrollment> findById(UUID id) {
        return jpaRepository.findByIdWithDetails(id);
    }

    @Override
    public Page<Enrollment> findAll(Specification<Enrollment> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }
}
