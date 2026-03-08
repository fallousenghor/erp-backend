package com.company.erp.modules.hr.application.service;

import com.company.erp.modules.hr.application.dto.request.CreatePerformanceReviewRequest;
import com.company.erp.modules.hr.application.dto.request.UpdatePerformanceReviewRequest;
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

import java.time.LocalDate;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PerformanceService {

    private final PerformanceReviewJpaRepository reviewRepository;
    private final ObjectiveJpaRepository objectiveRepository;

    // ==================== Performance Reviews - READ ====================

    // Simplified permission - allow authenticated users
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Page<PerformanceReview> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Page<PerformanceReview> getReviewsByEmployee(UUID employeeId, Pageable pageable) {
        return reviewRepository.findByEmployeeId(employeeId, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public PerformanceReview getReviewById(UUID id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Performance Review not found with id: " + id));
    }

    // ==================== Performance Reviews - CREATE ====================

    @PreAuthorize("isAuthenticated()")
    public PerformanceReview createReview(CreatePerformanceReviewRequest request) {
        PerformanceReview review = PerformanceReview.builder()
                .employeeId(request.getEmployeeId())
                .employeeName(request.getEmployeeName())
                .departmentId(request.getDepartmentId())
                .departmentName(request.getDepartmentName())
                .period(request.getPeriod())
                .rating(request.getRating())
                .objectivesCompleted(request.getObjectivesCompleted())
                .objectivesTotal(request.getObjectivesTotal())
                .feedback(request.getFeedback())
                .reviewerId(request.getReviewerId())
                .reviewerName(request.getReviewerName())
                .status(PerformanceReview.ReviewStatus.PENDING)
                .reviewedAt(LocalDate.now())
                .build();
        
        return reviewRepository.save(review);
    }

    // ==================== Performance Reviews - UPDATE ====================

    @PreAuthorize("isAuthenticated()")
    public PerformanceReview updateReview(UUID id, UpdatePerformanceReviewRequest request) {
        PerformanceReview review = getReviewById(id);
        
        // Update fields
        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getFeedback() != null) {
            review.setFeedback(request.getFeedback());
        }
        if (request.getObjectivesCompleted() != null) {
            review.setObjectivesCompleted(request.getObjectivesCompleted());
        }
        if (request.getObjectivesTotal() != null) {
            review.setObjectivesTotal(request.getObjectivesTotal());
        }
        if (request.getPeriod() != null) {
            review.setPeriod(request.getPeriod());
        }
        review.setReviewedAt(LocalDate.now());
        
        // Auto-update status based on rating
        if (review.getRating() >= 4) {
            review.setStatus(PerformanceReview.ReviewStatus.COMPLETED);
        } else if (review.getRating() >= 2) {
            review.setStatus(PerformanceReview.ReviewStatus.IN_PROGRESS);
        } else {
            review.setStatus(PerformanceReview.ReviewStatus.PENDING);
        }
        
        return reviewRepository.save(review);
    }

    @PreAuthorize("isAuthenticated()")
    public PerformanceReview updateReviewStatus(UUID id, PerformanceReview.ReviewStatus status) {
        PerformanceReview review = getReviewById(id);
        review.setStatus(status);
        return reviewRepository.save(review);
    }

    // ==================== Performance Reviews - DELETE ====================

    @PreAuthorize("isAuthenticated()")
    public void deleteReview(UUID id) {
        PerformanceReview review = getReviewById(id);
        reviewRepository.delete(review);
        log.info("Deleted performance review with id: {}", id);
    }

    // ==================== Objectives - READ ====================

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Page<Objective> getAllObjectives(Pageable pageable) {
        return objectiveRepository.findAll(pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Page<Objective> getObjectivesByEmployee(UUID employeeId, Pageable pageable) {
        return objectiveRepository.findByEmployeeId(employeeId, pageable);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Objective getObjectiveById(UUID id) {
        return objectiveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Objective not found with id: " + id));
    }

    // ==================== Objectives - CREATE ====================

    @PreAuthorize("isAuthenticated()")
    public Objective createObjective(Objective objective) {
        objective.setStatus(Objective.ObjectiveStatus.PENDING);
        objective.setAchieved(0);
        return objectiveRepository.save(objective);
    }

    // ==================== Objectives - UPDATE ====================

    @PreAuthorize("isAuthenticated()")
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

    @PreAuthorize("isAuthenticated()")
    public Objective updateObjective(UUID id, Objective objectiveDetails) {
        Objective objective = getObjectiveById(id);
        
        // Update fields
        objective.setTitle(objectiveDetails.getTitle());
        objective.setDescription(objectiveDetails.getDescription());
        objective.setTarget(objectiveDetails.getTarget());
        objective.setDueDate(objectiveDetails.getDueDate());
        
        // Recalculate status based on current achieved
        int achieved = objective.getAchieved();
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

    @PreAuthorize("isAuthenticated()")
    public Objective updateObjectiveStatus(UUID id, Objective.ObjectiveStatus status) {
        Objective objective = getObjectiveById(id);
        objective.setStatus(status);
        return objectiveRepository.save(objective);
    }

    // ==================== Objectives - DELETE ====================

    @PreAuthorize("isAuthenticated()")
    public void deleteObjective(UUID id) {
        Objective objective = getObjectiveById(id);
        objectiveRepository.delete(objective);
        log.info("Deleted objective with id: {}", id);
    }

    // ==================== Statistics & Analytics ====================

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public double getAverageRating() {
        return reviewRepository.findAll().stream()
                .mapToDouble(PerformanceReview::getRating)
                .average()
                .orElse(0.0);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public long getCompletedReviewsCount() {
        return reviewRepository.findAll().stream()
                .filter(r -> r.getStatus() == PerformanceReview.ReviewStatus.COMPLETED)
                .count();
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public long getAchievedObjectivesCount() {
        return objectiveRepository.findAll().stream()
                .filter(o -> o.getStatus() == Objective.ObjectiveStatus.ACHIEVED || o.getStatus() == Objective.ObjectiveStatus.EXCEEDED)
                .count();
    }
}

