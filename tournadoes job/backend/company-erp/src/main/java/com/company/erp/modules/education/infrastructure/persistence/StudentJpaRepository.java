package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentJpaRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByStudentCode(String studentCode);
    boolean existsByEmail(String email);
    boolean existsByStudentCode(String code);
}
