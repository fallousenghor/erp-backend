package com.company.erp.modules.projects.infrastructure.persistence;

import com.company.erp.modules.projects.domain.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectJpaRepository extends JpaRepository<Project, UUID> {
    Page<Project> findAll(Pageable pageable);
    Page<Project> findByDeletedFalse(Pageable pageable);
    Page<Project> findByStatus(Project.ProjectStatus status, Pageable pageable);
    long count();
    long countByStatus(Project.ProjectStatus status);
}

