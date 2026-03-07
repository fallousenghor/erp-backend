package com.company.erp.modules.education.infrastructure.persistence;

import com.company.erp.modules.education.domain.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseModuleJpaRepository extends JpaRepository<CourseModule, UUID> {

    List<CourseModule> findByProgramIdOrderByOrderIndexAsc(UUID programId);
}
