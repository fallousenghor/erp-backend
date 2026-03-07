package com.company.erp.modules.organization.infrastructure.persistence;

import com.company.erp.modules.organization.domain.model.Position;
import com.company.erp.modules.organization.domain.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PositionRepositoryImpl implements PositionRepository {

    private final PositionJpaRepository jpaRepository;

    @Override public Position save(Position p)                                    { return jpaRepository.save(p); }
    @Override public Optional<Position> findById(UUID id)                        { return jpaRepository.findById(id); }
    @Override public List<Position> findByDepartmentId(UUID deptId)              { return jpaRepository.findByDepartmentId(deptId); }
    @Override public boolean existsByTitleAndDepartmentId(String t, UUID deptId) { return jpaRepository.existsByTitleAndDepartmentId(t, deptId); }
}
