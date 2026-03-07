package com.company.erp.modules.organization.infrastructure.persistence;

import com.company.erp.modules.organization.domain.model.Department;
import com.company.erp.modules.organization.domain.repository.DepartmentRepository;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentJpaRepository jpaRepository;

    @Override
    public Department save(Department department) {
        return jpaRepository.save(department);
    }

    @Override
    public Optional<Department> findById(UUID id) {
        return jpaRepository.findByIdWithDetails(id);
    }

    @Override
    public Optional<Department> findByCode(String code) {
        return jpaRepository.findByCodeAndDeletedFalse(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCodeAndDeletedFalse(code);
    }

    @Override
    public Page<Department> findAll(Specification<Department> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }

    @Override
    public void softDelete(UUID id) {
        Department dept = jpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.DEPARTMENT_NOT_FOUND, id));
        dept.softDelete();
        jpaRepository.save(dept);
    }
}
