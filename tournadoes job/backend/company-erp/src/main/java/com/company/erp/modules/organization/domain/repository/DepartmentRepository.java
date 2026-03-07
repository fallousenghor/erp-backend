package com.company.erp.modules.organization.domain.repository;

import com.company.erp.modules.organization.domain.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    Optional<Department> findByCode(String code);

    boolean existsByCode(String code);

    Page<Department> findAll(Specification<Department> spec, Pageable pageable);

    void softDelete(UUID id);
}
