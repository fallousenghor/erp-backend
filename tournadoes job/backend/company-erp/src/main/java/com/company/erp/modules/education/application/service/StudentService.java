package com.company.erp.modules.education.application.service;

import com.company.erp.modules.education.application.dto.request.CreateStudentRequest;
import com.company.erp.modules.education.application.dto.response.StudentResponse;
import com.company.erp.modules.education.application.mapper.StudentMapper;
import com.company.erp.modules.education.domain.model.Student;
import com.company.erp.modules.education.domain.model.valueobject.StudentCode;
import com.company.erp.modules.education.domain.repository.StudentRepository;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Auditable(action = "CREATE_STUDENT", entity = "Student")
    @PreAuthorize("hasPermission(null, 'enrollment:manage')")
    public StudentResponse create(CreateStudentRequest request) {
        if (studentRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.CONFLICT,
                    "Email already registered: " + request.email());
        }

        Student student = Student.builder()
                .studentCode(StudentCode.generate().value())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email().toLowerCase())
                .phone(request.phone())
                .birthDate(request.birthDate())
                .address(request.address())
                .employeeId(request.employeeId())
                .build();

        return studentMapper.toResponse(studentRepository.save(student));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public StudentResponse findById(UUID id) {
        return studentMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public PageResponse<StudentResponse> findAll(Pageable pageable) {
        return PageResponse.from(studentRepository.findAll(pageable).map(studentMapper::toResponse));
    }

    public Student findOrThrow(UUID id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.STUDENT_NOT_FOUND, id));
    }
}
