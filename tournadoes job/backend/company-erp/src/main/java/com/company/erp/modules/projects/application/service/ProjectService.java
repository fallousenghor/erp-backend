package com.company.erp.modules.projects.application.service;

import com.company.erp.modules.projects.domain.model.Project;
import com.company.erp.modules.projects.infrastructure.persistence.ProjectJpaRepository;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectJpaRepository projectRepository;

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Page<Project> getAllProjects(Pageable pageable) {
        return projectRepository.findByDeletedFalse(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Project getProjectById(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, id.toString()));
    }

    @PreAuthorize("hasPermission(null, 'project:create')")
    public Project createProject(Project project) {
        project.setStatus(Project.ProjectStatus.PLANNING);
        return projectRepository.save(project);
    }

    @PreAuthorize("hasPermission(null, 'project:update')")
    public Project updateProject(UUID id, Project updatedProject) {
        Project existing = getProjectById(id);
        
        existing.setName(updatedProject.getName());
        existing.setDescription(updatedProject.getDescription());
        existing.setClientName(updatedProject.getClientName());
        existing.setStatus(updatedProject.getStatus());
        existing.setStartDate(updatedProject.getStartDate());
        existing.setEndDate(updatedProject.getEndDate());
        existing.setBudget(updatedProject.getBudget());
        existing.setDepartmentId(updatedProject.getDepartmentId());
        existing.setDepartmentName(updatedProject.getDepartmentName());
        existing.setManagerId(updatedProject.getManagerId());
        existing.setManagerName(updatedProject.getManagerName());
        
        return projectRepository.save(existing);
    }

    @PreAuthorize("hasPermission(null, 'project:delete')")
    public void deleteProject(UUID id) {
        Project project = getProjectById(id);
        project.setDeleted(true);
        projectRepository.save(project);
    }
}

