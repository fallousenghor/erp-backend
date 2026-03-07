package com.company.erp.modules.organization.domain.repository;

import com.company.erp.modules.organization.domain.model.Position;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PositionRepository {

    Position save(Position position);

    Optional<Position> findById(UUID id);

    List<Position> findByDepartmentId(UUID departmentId);

    boolean existsByTitleAndDepartmentId(String title, UUID departmentId);
}
