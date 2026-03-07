package com.company.erp.modules.hr.application.service;

import com.company.erp.modules.hr.domain.model.Objective;
import com.company.erp.modules.hr.domain.model.PerformanceReview;
import com.company.erp.modules.hr.infrastructure.persistence.ObjectiveJpaRepository;
import com.company.erp.modules.hr.infrastructure.persistence.PerformanceReviewJpaRepository;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PerformanceService {

    private final PerformanceReviewJpaRepository reviewRepository;
    private final ObjectiveJpaRepository objectiveRepository;

    // ==================== Performance Reviews ====================

    @PreAuthorize("hasPermission(null, 'performance:view')")
    @Transactional(readOnly = true)
    public Page<PerformanceReview> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @PreAuthorize("hasPermission(null, 'performance:view')")
    @Transactional(readOnly = true)
    public Page<PerformanceReview> getReviewsByEmployee(UUID employeeId, Pageable pageable) {
        return reviewRepository.findByEmployeeId(employeeId, pageable);
    }

    @PreAuthorize("hasPermission(null, 'performance:view')")
    @Transactional(readOnly = true)
    public PerformanceReview getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, id.toString()));
    }

    @PreAuthorize("hasPermission(null, 'performance:create')")
    public PerformanceReview createReview(PerformanceReview review) {
        review.setStatus(PerformanceReview.ReviewStatus.PENDING);
        return reviewRepository.save(review);
    }

    // ==================== Objectives ====================

    @PreAuthorize("hasPermission(null, 'performance:view')")
    @Transactional(readOnly = true)
    public Page<Objective> getAllObjectives(Pageable pageable) {
        return objectiveRepository.findAll(pageable);
    }

    @PreAuthorize("hasPermission(null, 'performance:view')")
    @Transactional(readOnly = true)
    public Page<Objective> getObjectivesByEmployee(UUID employeeId, Pageable pageable) {
        return objectiveRepository.findByEmployeeId(employeeId, pageable);
    }

    @PreAuthorize("hasPermission(null, 'performance:view')")
    @Transactional(readOnly = true)
    public Objective getObjectiveById(UUID id) {
        return objectiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, id.toString()));
    }

    @PreAuthorize("hasPermission(null, 'performance:create')")
    public Objective createObjective(Objective objective) {
        objective.setStatus(Objective.ObjectiveStatus.PENDING);
        return objectiveRepository.save(objective);
    }

    @PreAuthorize("hasPermission(null, 'performance:update')")
    public Objective updateObjectiveProgress(UUID id, int achieved) {
        Objective objective = getObjectiveById(id);
        objective.setAchieved(achieved);
        
        // Update status based on progress
        if (achieved >= objective.getTarget()) {
            objective.setStatus(Objective.ObjectiveStatus.ACHIEVED);
            if (achieved > objective.getTarget()) {
                objective.setStatus(Objective.ObjectiveStatus.EXCEEDED);
            }
        } else if (achieved >= objective.getTarget() * 0.7) {
            objective.setStatus(Objective.ObjectiveStatus.AT_RISK);
        } else {
            objective.setStatus(Objective.ObjectiveStatus.PENDING);
        }
        
        return objectiveRepository.save(objective);
    }
}

