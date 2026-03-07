package com.company.erp.modules.education.application.mapper;

import com.company.erp.modules.education.application.dto.response.EnrollmentResponse;
import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.ModuleGrade;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(target = "studentId",    source = "student.id")
    @Mapping(target = "studentName",  expression = "java(enrollment.getStudent().getFullName())")
    @Mapping(target = "studentCode",  source = "student.studentCode")
    @Mapping(target = "programId",    source = "program.id")
    @Mapping(target = "programTitle", source = "program.title")
    @Mapping(target = "grades",       source = "moduleGrades")
    EnrollmentResponse toResponse(Enrollment enrollment);

    @Mapping(target = "moduleId",    source = "module.id")
    @Mapping(target = "moduleTitle", source = "module.title")
    @Mapping(target = "score",       source = "grade.score")
    @Mapping(target = "maxScore",    source = "grade.maxScore")
    @Mapping(target = "letterGrade", expression = "java(moduleGrade.getGrade().letterGrade())")
    @Mapping(target = "passed",      expression = "java(moduleGrade.getGrade().isPassing())")
    EnrollmentResponse.GradeResponse toGradeResponse(ModuleGrade moduleGrade);
}
