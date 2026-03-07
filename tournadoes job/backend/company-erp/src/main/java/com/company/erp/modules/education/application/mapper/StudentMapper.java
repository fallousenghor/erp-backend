package com.company.erp.modules.education.application.mapper;

import com.company.erp.modules.education.application.dto.response.StudentResponse;
import com.company.erp.modules.education.domain.model.Student;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse toResponse(Student student);
}
