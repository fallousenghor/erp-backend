package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.Employee;
import com.company.erp.modules.hr.domain.model.valueobject.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeJpaRepository
        extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    boolean existsByEmail(String email);
    boolean existsByEmployeeNumber(String employeeNumber);
    long countByDepartmentId(UUID departmentId);
    long countByStatus(EmployeeStatus status);
}
