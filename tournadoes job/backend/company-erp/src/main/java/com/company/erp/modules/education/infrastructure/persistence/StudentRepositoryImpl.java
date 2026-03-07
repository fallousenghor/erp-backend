package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.Student;
import com.company.erp.modules.education.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

    private final StudentJpaRepository jpaRepository;

    @Override public Student save(Student s)                           { return jpaRepository.save(s); }
    @Override public Optional<Student> findById(UUID id)              { return jpaRepository.findById(id); }
    @Override public Optional<Student> findByStudentCode(String code) { return jpaRepository.findByStudentCode(code); }
    @Override public boolean existsByEmail(String email)              { return jpaRepository.existsByEmail(email); }
    @Override public boolean existsByStudentCode(String code)         { return jpaRepository.existsByStudentCode(code); }
    @Override public Page<Student> findAll(Pageable pageable)         { return jpaRepository.findAll(pageable); }
}
