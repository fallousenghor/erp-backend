package com.company.erp.modules.education.application.service;

import com.company.erp.modules.education.application.dto.response.TeacherResponse;
import com.company.erp.modules.education.domain.model.Teacher;
import com.company.erp.modules.education.domain.repository.TeacherRepository;
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
public class TeacherService {

    private final TeacherRepository teacherRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public TeacherResponse findById(UUID id) {
        Teacher t = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TEACHER_NOT_FOUND, id));
        return mapToResponse(t);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public PageResponse<TeacherResponse> findAll(Pageable pageable) {
        return PageResponse.from(teacherRepository.findAll(pageable).map(this::mapToResponse));
    }

    private TeacherResponse mapToResponse(Teacher t) {
        return new TeacherResponse(
                t.getId(), t.getFirstName(), t.getLastName(), t.getEmail(),
                t.getPhone(), t.getSpecialization(), t.isActive(),
                t.getEmployeeId(), t.getCreatedAt());
    }
}
