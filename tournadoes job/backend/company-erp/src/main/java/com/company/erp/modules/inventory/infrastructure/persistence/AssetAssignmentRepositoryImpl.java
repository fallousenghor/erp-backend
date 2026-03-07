package com.company.erp.modules.inventory.infrastructure.persistence;

import com.company.erp.modules.inventory.domain.model.AssetAssignment;
import com.company.erp.modules.inventory.domain.repository.AssetAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AssetAssignmentRepositoryImpl implements AssetAssignmentRepository {

    private final AssetAssignmentJpaRepository jpaRepository;

    @Override public AssetAssignment save(AssetAssignment a)              { return jpaRepository.save(a); }
    @Override public List<AssetAssignment> findByEmployeeId(UUID id)      { return jpaRepository.findByEmployeeId(id); }
    @Override public List<AssetAssignment> findActiveByEmployeeId(UUID id){ return jpaRepository.findByEmployeeIdAndActiveTrue(id); }
}
