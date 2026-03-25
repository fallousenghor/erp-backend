package com.company.erp.modules.organization.application.service;

import com.company.erp.modules.organization.application.command.*;
import com.company.erp.modules.organization.application.dto.response.DepartmentResponse;
import com.company.erp.modules.organization.application.dto.response.DepartmentDetailResponse;
import com.company.erp.modules.organization.application.dto.response.DepartmentStatsResponse;
import com.company.erp.modules.organization.application.mapper.DepartmentMapper;
import com.company.erp.modules.organization.application.query.GetDepartmentsQuery;
import com.company.erp.modules.organization.domain.event.DepartmentCreatedEvent;
import com.company.erp.modules.organization.domain.event.DepartmentHeadChangedEvent;
import com.company.erp.modules.organization.domain.event.DepartmentUpdatedEvent;
import com.company.erp.modules.organization.domain.model.Department;
import com.company.erp.modules.organization.domain.repository.DepartmentRepository;
import com.company.erp.modules.organization.infrastructure.persistence.specification.DepartmentSpecification;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.modules.hr.domain.repository.EmployeeRepository;
import com.company.erp.modules.organization.domain.repository.PositionRepository;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final DepartmentMapper departmentMapper;
    private final DomainEventPublisher eventPublisher;

    // ── Commands ─────────────────────────────────────────────────────────────

    @Auditable(action = "CREATE_DEPARTMENT", entity = "Department")
    @PreAuthorize("hasPermission(null, 'department:create')")
    public DepartmentResponse create(CreateDepartmentCommand command) {
        if (departmentRepository.existsByCode(command.code())) {
            throw new BusinessException(ErrorCode.DEPARTMENT_CODE_ALREADY_EXISTS,
                    "Code already exists: " + command.code());
        }

        Department department = Department.builder()
                .name(command.name())
                .code(command.code().toUpperCase())
                .description(command.description())
                .budget(command.budget() != null ? command.budget() : BigDecimal.ZERO)
                .build();

        department = departmentRepository.save(department);
        eventPublisher.publish(new DepartmentCreatedEvent(
                department.getId(), department.getName(), department.getCode()));

        log.info("Department created: [{}] {}", department.getCode(), department.getName());
        return departmentMapper.toResponse(department);
    }

    @Auditable(action = "UPDATE_DEPARTMENT", entity = "Department")
    @PreAuthorize("hasPermission(null, 'department:update')")
    public DepartmentResponse update(UpdateDepartmentCommand command) {
        Department department = findOrThrow(command.id());

        if (command.name() != null && !command.name().isBlank()) {
            department.setName(command.name());
        }
        if (command.description() != null) {
            department.setDescription(command.description());
        }
        if (command.active() != null) {
            department.setActive(command.active());
        }
        // Budget is now handled via dedicated updateBudget method or in command

        department = departmentRepository.save(department);
        eventPublisher.publish(new DepartmentUpdatedEvent(department.getId(), department.getName()));
        return departmentMapper.toResponse(department);
    }

    @Auditable(action = "UPDATE_DEPARTMENT_BUDGET", entity = "Department")
    @PreAuthorize("hasPermission(null, 'department:update')")
    public DepartmentResponse updateBudget(UUID departmentId, BigDecimal newBudget) {
        Department department = findOrThrow(departmentId);
        
        if (newBudget == null || newBudget.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Budget must be non-negative");
        }
        
        department.updateBudget(newBudget);
        department = departmentRepository.save(department);
        
        log.info("Department budget updated: {} - New budget: {}", departmentId, newBudget);
        return departmentMapper.toResponse(department);
    }

    @Auditable(action = "DELETE_DEPARTMENT", entity = "Department")
    @PreAuthorize("hasPermission(null, 'department:delete')")
    public void delete(DeleteDepartmentCommand command) {
        Department department = findOrThrow(command.id());
        
        // Check dependencies before deletion
        long employeeCount = employeeRepository.countByDepartmentId(command.id());
        long positionCount = positionRepository.findByDepartmentId(command.id()).size();
        
        if (employeeCount > 0 || positionCount > 0) {
            throw new BusinessException(ErrorCode.CONFLICT,
                String.format("Cannot delete department. Dependencies: Employees=%d, Positions=%d", 
                    employeeCount, positionCount));
        }
        
        department.softDelete();
        departmentRepository.save(department);
        log.info("Department soft-deleted: {}", command.id());
    }

    @Auditable(action = "RESTORE_DEPARTMENT", entity = "Department")
    @PreAuthorize("hasPermission(null, 'department:restore')")
    public DepartmentResponse restore(UUID departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND, departmentId));
        
        if (!department.isDeleted()) {
            throw new BusinessException(ErrorCode.CONFLICT, "Department is not deleted");
        }
        
        department.setDeleted(false);
        department.setActive(true);
        department.setDeletedAt(null);
        
        department = departmentRepository.save(department);
        log.info("Department restored: {}", departmentId);
        return departmentMapper.toResponse(department);
    }

    @Auditable(action = "ASSIGN_DEPARTMENT_HEAD", entity = "DepartmentHead")
    @PreAuthorize("hasPermission(null, 'department:update')")
    public DepartmentResponse assignHead(AssignDepartmentHeadCommand command) {
        Department department = findOrThrow(command.departmentId());
        
        // Validate that employee exists
        boolean employeeExists = employeeRepository.findById(command.employeeId()).isPresent();
        if (!employeeExists) {
            throw new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND, command.employeeId());
        }
        
        department.assignHead(command.employeeId(), command.employeeName(), command.startDate());
        department = departmentRepository.save(department);

        eventPublisher.publish(new DepartmentHeadChangedEvent(
                department.getId(), command.employeeId(), command.employeeName()));

        return departmentMapper.toResponse(department);
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'department:read')")
    public DepartmentResponse findById(UUID id) {
        return departmentMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'department:read')")
    public DepartmentDetailResponse findDetailById(UUID id) {
        Department department = findOrThrow(id);
        long employeeCount = employeeRepository.countByDepartmentId(id);
        
        DepartmentDetailResponse response = departmentMapper.toDetailResponse(department);
        
        // Build response with employee count
        return new DepartmentDetailResponse(
                response.id(),
                response.name(),
                response.code(),
                response.description(),
                response.active(),
                response.deleted(),
                response.deletedAt(),
                response.budget(),
                response.currentHead(),
                response.headHistory(),
                (int) employeeCount,
                response.positionCount(),
                response.createdAt(),
                response.updatedAt()
        );
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'department:read')")
    public DepartmentStatsResponse getStats(UUID id) {
        Department department = findOrThrow(id);
        long employeeCount = employeeRepository.countByDepartmentId(id);
        
        // Count active positions
        int openPositions = 0;
        if (department.getPositions() != null) {
            openPositions = (int) department.getPositions().stream()
                    .filter(p -> p.isActive())
                    .count();
        }
        
        return new DepartmentStatsResponse(
                department.getId(),
                department.getName(),
                department.getCode(),
                (int) employeeCount,
                (int) employeeCount, // Assuming all are active for now
                department.getPositions() != null ? department.getPositions().size() : 0,
                openPositions,
                department.getBudget() != null ? department.getBudget() : BigDecimal.ZERO,
                BigDecimal.ZERO, // utilization - to be calculated from expenses
                department.getCurrentHead() != null,
                department.getCurrentHead() != null ? department.getCurrentHead().getEmployeeName() : null
        );
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'department:read')")
    public PageResponse<DepartmentResponse> findAll(GetDepartmentsQuery query) {
        Specification<Department> spec = DepartmentSpecification.build(
                query.name(), query.code(), query.active(), 
                query.minBudget(), query.maxBudget());
        return PageResponse.from(
                departmentRepository.findAll(spec, query.pageable())
                        .map(departmentMapper::toResponse));
    }

    // ── Helpers ─────────────────────────────────────────────────────────────

    private Department findOrThrow(UUID id) {
        return departmentRepository.findById(id)
                .filter(d -> !d.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND, id));
    }
}
