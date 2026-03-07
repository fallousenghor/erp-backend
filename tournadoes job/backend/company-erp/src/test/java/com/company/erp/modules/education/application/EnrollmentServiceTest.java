package com.company.erp.modules.education.application;

import com.company.erp.modules.education.application.dto.request.EnrollStudentRequest;
import com.company.erp.modules.education.application.service.EnrollmentService;
import com.company.erp.modules.education.domain.model.Student;
import com.company.erp.modules.education.domain.model.TrainingProgram;
import com.company.erp.modules.education.domain.repository.EnrollmentRepository;
import com.company.erp.modules.education.domain.repository.StudentRepository;
import com.company.erp.modules.education.domain.repository.TrainingProgramRepository;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private TrainingProgramRepository programRepository;
    @Mock private DomainEventPublisher eventPublisher;

    @InjectMocks private EnrollmentService enrollmentService;

    @Test
    void enroll_whenAlreadyEnrolled_throwsBusinessException() {
        UUID studentId = UUID.randomUUID();
        UUID programId = UUID.randomUUID();

        Student student = mock(Student.class);
        TrainingProgram program = mock(TrainingProgram.class);
        when(program.isFull(anyLong())).thenReturn(false);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(programRepository.findById(programId)).thenReturn(Optional.of(program));
        when(enrollmentRepository.existsByStudentIdAndProgramId(studentId, programId)).thenReturn(true);

        EnrollStudentRequest request = new EnrollStudentRequest(studentId, programId, LocalDate.now());
        assertThrows(BusinessException.class, () -> enrollmentService.enroll(request));
    }
}
