package com.company.erp.modules.hr.domain.repository;

import com.company.erp.modules.hr.domain.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(UUID id);

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    boolean existsByEmail(String email);

    boolean existsByEmployeeNumber(String employeeNumber);

    Page<Employee> findAll(Specification<Employee> spec, Pageable pageable);

    long countByDepartmentId(UUID departmentId);
}
