package com.company.erp.modules.inventory.infrastructure.persistence;

import com.company.erp.modules.inventory.domain.model.AssetAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssetAssignmentJpaRepository extends JpaRepository<AssetAssignment, UUID> {

    List<AssetAssignment> findByEmployeeId(UUID employeeId);

    List<AssetAssignment> findByEmployeeIdAndActiveTrue(UUID employeeId);
}
