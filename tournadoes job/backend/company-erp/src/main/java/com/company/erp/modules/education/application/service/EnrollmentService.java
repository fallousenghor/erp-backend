package com.company.erp.modules.education.application.service;

import com.company.erp.modules.education.application.dto.request.EnrollStudentRequest;
import com.company.erp.modules.education.application.dto.request.RecordGradeRequest;
import com.company.erp.modules.education.application.dto.response.EnrollmentResponse;
import com.company.erp.modules.education.application.mapper.EnrollmentMapper;
import com.company.erp.modules.education.domain.event.EnrollmentCompletedEvent;
import com.company.erp.modules.education.domain.event.GradeRecordedEvent;
import com.company.erp.modules.education.domain.event.StudentEnrolledEvent;
import com.company.erp.modules.education.domain.model.*;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import com.company.erp.modules.education.domain.repository.EnrollmentRepository;
import com.company.erp.modules.education.infrastructure.persistence.CourseModuleJpaRepository;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final TrainingProgramService programService;
    private final StudentService studentService;
    private final CourseModuleJpaRepository moduleRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final DomainEventPublisher eventPublisher;

    @Auditable(action = "ENROLL_STUDENT", entity = "Enrollment")
    @PreAuthorize("hasPermission(null, 'enrollment:manage')")
    public EnrollmentResponse enroll(EnrollStudentRequest request) {
        Student student = studentService.findOrThrow(request.studentId());
        TrainingProgram program = programService.findOrThrow(request.programId());

        if (enrollmentRepository.existsByStudentIdAndProgramId(
                request.studentId(), request.programId())) {
            throw new BusinessException(ErrorCode.STUDENT_ALREADY_ENROLLED);
        }

        long currentCount = enrollmentRepository.countByProgramIdAndStatus(
                request.programId(), EnrollmentStatus.ACTIVE);
        if (program.isFull(currentCount)) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    "Program '" + program.getTitle() + "' is full");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .program(program)
                .enrollmentDate(request.enrollmentDate() != null
                        ? request.enrollmentDate() : LocalDate.now())
                .status(EnrollmentStatus.ACTIVE)
                .build();

        enrollment = enrollmentRepository.save(enrollment);

        eventPublisher.publish(new StudentEnrolledEvent(
                enrollment.getId(), student.getId(), student.getFullName(),
                program.getId(), program.getTitle()));

        log.info("Student [{}] enrolled in program [{}]",
                student.getStudentCode(), program.getTitle());
        return enrollmentMapper.toResponse(enrollment);
    }

    @Auditable(action = "RECORD_GRADE", entity = "Enrollment")
    @PreAuthorize("hasPermission(null, 'grade:record')")
    public EnrollmentResponse recordGrade(UUID enrollmentId, RecordGradeRequest request) {
        Enrollment enrollment = findOrThrow(enrollmentId);

        CourseModule module = moduleRepository.findById(request.moduleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.BAD_REQUEST, "Module not found: " + request.moduleId()));

        enrollment.recordGrade(module, request.score(), request.maxScore());
        enrollment = enrollmentRepository.save(enrollment);

        eventPublisher.publish(new GradeRecordedEvent(
                enrollmentId, request.moduleId(),
                enrollment.getStudent().getId(), request.score()));

        return enrollmentMapper.toResponse(enrollment);
    }

    @Auditable(action = "COMPLETE_ENROLLMENT", entity = "Enrollment")
    @PreAuthorize("hasPermission(null, 'enrollment:manage')")
    public EnrollmentResponse complete(UUID enrollmentId) {
        Enrollment enrollment = findOrThrow(enrollmentId);
        enrollment.complete();
        enrollment = enrollmentRepository.save(enrollment);

        eventPublisher.publish(new EnrollmentCompletedEvent(
                enrollment.getId(), enrollment.getStudent().getId(),
                enrollment.getFinalAverage(), enrollment.isPassed()));

        log.info("Enrollment [{}] completed. Passed: {}, Average: {}",
                enrollmentId, enrollment.isPassed(), enrollment.getFinalAverage());
        return enrollmentMapper.toResponse(enrollment);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public EnrollmentResponse findById(UUID id) {
        return enrollmentMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public PageResponse<EnrollmentResponse> findByStudent(UUID studentId, Pageable pageable) {
        return PageResponse.from(
                enrollmentRepository.findByStudentId(studentId, pageable)
                        .map(enrollmentMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public PageResponse<EnrollmentResponse> findByProgram(UUID programId, Pageable pageable) {
        return PageResponse.from(
                enrollmentRepository.findByProgramId(programId, pageable)
                        .map(enrollmentMapper::toResponse));
    }

    private Enrollment findOrThrow(UUID id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ENROLLMENT_NOT_FOUND, id));
    }
}
