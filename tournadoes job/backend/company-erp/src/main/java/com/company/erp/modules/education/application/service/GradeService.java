package com.company.erp.modules.education.application.service;

import com.company.erp.modules.education.application.dto.request.CreateGradeRequest;
import com.company.erp.modules.education.application.dto.response.GradeStatsResponse;
import com.company.erp.modules.education.application.dto.response.StudentAverageResponse;
import com.company.erp.modules.education.domain.model.CourseModule;
import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.ModuleGrade;
import com.company.erp.modules.education.domain.repository.EnrollmentRepository;
import com.company.erp.modules.education.infrastructure.persistence.CourseModuleJpaRepository;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseModuleJpaRepository moduleRepository;
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasPermission(null, 'grade:read')")
    public GradeStatsResponse getStats(UUID programId) {
        var pageable = Pageable.ofSize(Integer.MAX_VALUE);
        var enrollments = enrollmentRepository.findByProgramId(programId, pageable).getContent();
        if (enrollments.isEmpty()) {
            return GradeStatsResponse.builder()
                .average(BigDecimal.ZERO)
                .highest(BigDecimal.ZERO)
                .lowest(BigDecimal.ZERO)
                .passRate(BigDecimal.ZERO)
                .totalStudents(0L)
                .build();
        }

        var allGrades = enrollments.stream()
            .flatMap(e -> e.getModuleGrades().stream())
            .map(mg -> mg.getGrade().outOf20())
            .collect(Collectors.toList());

        BigDecimal average = allGrades.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(allGrades.size()), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal highest = allGrades.stream().max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        BigDecimal lowest = allGrades.stream().min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        long passed = allGrades.stream().filter(g -> g.compareTo(BigDecimal.TEN) >= 0).count();
        BigDecimal passRate = BigDecimal.valueOf(passed * 100.0 / allGrades.size()).setScale(2, BigDecimal.ROUND_HALF_UP);

        return GradeStatsResponse.builder()
            .average(average)
            .highest(highest)
            .lowest(lowest)
            .passRate(passRate)
            .totalStudents((long) enrollments.size())
            .build();
    }

    @PreAuthorize("hasPermission(null, 'grade:read')")
    public List<StudentAverageResponse> getStudentsWithAverages(UUID programId) {
        return List.of(); // Stub to compile
    }

    @Transactional
    @PreAuthorize("hasPermission(null, 'grade:write')")
    public void createGrade(CreateGradeRequest request) {
        CourseModule module = moduleRepository.findById(request.getModuleId())
            .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Module not found: " + request.getModuleId()));

        log.warn("Grade creation stub - integrate with Enrollment.recordGrade");
    }
}

