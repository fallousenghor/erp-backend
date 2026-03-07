package com.company.erp.modules.organization.infrastructure.persistence;

import com.company.erp.modules.organization.domain.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentJpaRepository
        extends JpaRepository<Department, UUID>, JpaSpecificationExecutor<Department> {

    Optional<Department> findByCodeAndDeletedFalse(String code);
    boolean existsByCodeAndDeletedFalse(String code);
    long countByDeletedFalse();

    @Query("""
            SELECT d FROM Department d
            LEFT JOIN FETCH d.positions
            LEFT JOIN FETCH d.heads h
            WHERE d.id = :id AND d.deleted = false
            """)
    Optional<Department> findByIdWithDetails(@Param("id") UUID id);
}
