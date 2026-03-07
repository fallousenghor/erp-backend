package com.company.erp.modules.education.application.mapper;

import com.company.erp.modules.education.application.dto.response.TrainingProgramResponse;
import com.company.erp.modules.education.domain.model.CourseModule;
import com.company.erp.modules.education.domain.model.TrainingProgram;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgramMapper {

    @Mapping(target = "totalHours",          expression = "java(program.getTotalHours())")
    @Mapping(target = "currentEnrollments",  constant = "0L")
    @Mapping(target = "modules",             source = "modules")
    TrainingProgramResponse toResponse(TrainingProgram program);

    @Mapping(target = "teacherName",
             expression = "java(module.getTeacher() != null ? module.getTeacher().getFullName() : null)")
    TrainingProgramResponse.ModuleResponse toModuleResponse(CourseModule module);
}
