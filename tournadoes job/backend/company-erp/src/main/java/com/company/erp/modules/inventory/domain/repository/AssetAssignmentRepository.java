package com.company.erp.modules.inventory.domain.repository;

import com.company.erp.modules.inventory.domain.model.AssetAssignment;

import java.util.List;
import java.util.UUID;

public interface AssetAssignmentRepository {
    AssetAssignment save(AssetAssignment assignment);
    List<AssetAssignment> findByEmployeeId(UUID employeeId);
    List<AssetAssignment> findActiveByEmployeeId(UUID employeeId);
}
