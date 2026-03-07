package com.company.erp.modules.education.domain.repository;

import com.company.erp.modules.education.domain.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TeacherRepository {
    Teacher save(Teacher teacher);
    Optional<Teacher> findById(UUID id);
    boolean existsByEmail(String email);
    Page<Teacher> findAll(Pageable pageable);
}
