package com.company.erp.modules.inventory.application.service;

import com.company.erp.modules.inventory.application.dto.response.AssetAssignmentResponse;
import com.company.erp.modules.inventory.domain.model.AssetAssignment;
import com.company.erp.modules.inventory.domain.repository.AssetAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetAssignmentService {

    private final AssetAssignmentRepository assignmentRepository;

    @PreAuthorize("hasPermission(null, 'asset:read')")
    public List<AssetAssignmentResponse> findByEmployee(UUID employeeId) {
        return assignmentRepository.findByEmployeeId(employeeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @PreAuthorize("hasPermission(null, 'asset:read')")
    public List<AssetAssignmentResponse> findActiveByEmployee(UUID employeeId) {
        return assignmentRepository.findActiveByEmployeeId(employeeId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private AssetAssignmentResponse toResponse(AssetAssignment a) {
        return new AssetAssignmentResponse(
                a.getId(),
                a.getAsset().getId(),
                a.getAsset().getAssetCode(),
                a.getAsset().getName(),
                a.getEmployeeId(),
                a.getEmployeeName(),
                a.getAssignedDate(),
                a.getReturnedDate(),
                a.isActive(),
                a.getNotes(),
                a.getCreatedAt()
        );
    }
}
