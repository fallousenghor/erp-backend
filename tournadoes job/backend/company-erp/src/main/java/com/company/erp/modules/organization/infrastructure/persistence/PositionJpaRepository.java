package com.company.erp.modules.organization.infrastructure.persistence;

import com.company.erp.modules.organization.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PositionJpaRepository extends JpaRepository<Position, UUID> {

    List<Position> findByDepartmentId(UUID departmentId);

    boolean existsByTitleAndDepartmentId(String title, UUID departmentId);
}
