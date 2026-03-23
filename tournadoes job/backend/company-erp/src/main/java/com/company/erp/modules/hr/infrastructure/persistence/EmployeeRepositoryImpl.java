package com.company.erp.modules.hr.infrastructure.persistence;

import com.company.erp.modules.hr.domain.model.Employee;
import com.company.erp.modules.hr.domain.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository jpaRepository;

    @Override public Employee save(Employee e)                          { return jpaRepository.save(e); }
    @Override public Optional<Employee> findById(UUID id)              { return jpaRepository.findById(id); }
    @Override public Optional<Employee> findByEmployeeNumber(String n) { return jpaRepository.findByEmployeeNumber(n); }
    @Override public boolean existsByEmail(String email)               { return jpaRepository.existsByEmail(email); }
    @Override public boolean existsByEmployeeNumber(String n)          { return jpaRepository.existsByEmployeeNumber(n); }
    @Override public long countByDepartmentId(UUID deptId)             { return jpaRepository.countByDepartmentId(deptId); }

@Override
    public Page<Employee> findAll(Specification<Employee> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }

    @Override
    public void delete(Employee employee) {
        jpaRepository.delete(employee);
    }
}
