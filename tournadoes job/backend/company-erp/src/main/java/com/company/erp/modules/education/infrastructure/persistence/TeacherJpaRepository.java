package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeacherJpaRepository extends JpaRepository<Teacher, UUID> {
    boolean existsByEmail(String email);
}
