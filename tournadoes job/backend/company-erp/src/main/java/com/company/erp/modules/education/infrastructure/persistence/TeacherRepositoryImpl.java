package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.Teacher;
import com.company.erp.modules.education.domain.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TeacherRepositoryImpl implements TeacherRepository {

    private final TeacherJpaRepository jpaRepository;

    @Override public Teacher save(Teacher t)                  { return jpaRepository.save(t); }
    @Override public Optional<Teacher> findById(UUID id)      { return jpaRepository.findById(id); }
    @Override public boolean existsByEmail(String email)      { return jpaRepository.existsByEmail(email); }
    @Override public Page<Teacher> findAll(Pageable pageable) { return jpaRepository.findAll(pageable); }
}
