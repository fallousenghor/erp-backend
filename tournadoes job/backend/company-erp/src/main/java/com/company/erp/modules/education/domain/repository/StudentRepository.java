package com.company.erp.modules.education.domain.repository;

import com.company.erp.modules.education.domain.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository {
    Student save(Student student);
    Optional<Student> findById(UUID id);
    Optional<Student> findByStudentCode(String code);
    boolean existsByEmail(String email);
    boolean existsByStudentCode(String code);
    Page<Student> findAll(Pageable pageable);
}
