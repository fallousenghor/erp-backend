package com.company.erp.modules.organization.application.service;

import com.company.erp.modules.organization.application.dto.response.PositionResponse;
import com.company.erp.modules.organization.application.mapper.PositionMapper;
import com.company.erp.modules.organization.domain.model.Department;
import com.company.erp.modules.organization.domain.model.Position;
import com.company.erp.modules.organization.domain.repository.DepartmentRepository;
import com.company.erp.modules.organization.domain.repository.PositionRepository;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionService {

    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionMapper positionMapper;

    @PreAuthorize("hasPermission(null, 'department:update')")
    public PositionResponse create(UUID departmentId, String title,
                                    String description, Double minSalary, Double maxSalary) {
        if (positionRepository.existsByTitleAndDepartmentId(title, departmentId)) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    "Position '" + title + "' already exists in this department");
        }

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.DEPARTMENT_NOT_FOUND, departmentId));

        Position position = Position.builder()
                .title(title)
                .description(description)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .department(department)
                .build();

        return positionMapper.toResponse(positionRepository.save(position));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'department:read')")
    public List<PositionResponse> findByDepartment(UUID departmentId) {
        return positionRepository.findByDepartmentId(departmentId)
                .stream()
                .map(positionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'department:read')")
    public PositionResponse findById(UUID id) {
        return positionMapper.toResponse(
                positionRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(
                                ErrorCode.POSITION_NOT_FOUND, id)));
    }
}
