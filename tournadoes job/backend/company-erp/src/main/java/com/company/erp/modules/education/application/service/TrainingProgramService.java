package com.company.erp.modules.education.application.service;

import com.company.erp.modules.education.application.dto.request.CreateProgramRequest;
import com.company.erp.modules.education.application.dto.response.TrainingProgramResponse;
import com.company.erp.modules.education.application.mapper.ProgramMapper;
import com.company.erp.modules.education.domain.event.ProgramCreatedEvent;
import com.company.erp.modules.education.domain.model.TrainingProgram;
import com.company.erp.modules.education.domain.repository.TrainingProgramRepository;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class TrainingProgramService {

    private final TrainingProgramRepository programRepository;
    private final ProgramMapper programMapper;
    private final DomainEventPublisher eventPublisher;

    @Auditable(action = "CREATE_PROGRAM", entity = "TrainingProgram")
    @PreAuthorize("hasPermission(null, 'program:create')")
    public TrainingProgramResponse create(CreateProgramRequest request) {
        TrainingProgram program = TrainingProgram.builder()
                .title(request.title())
                .description(request.description())
                .level(request.level())
                .durationWeeks(request.durationWeeks())
                .maxStudents(request.maxStudents())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .passingScore(request.passingScore() != null
                        ? request.passingScore() : new java.math.BigDecimal("10.00"))
                .build();

        program = programRepository.save(program);
        eventPublisher.publish(new ProgramCreatedEvent(program.getId(), program.getTitle()));

        log.info("Training program created: {}", program.getTitle());
        return programMapper.toResponse(program);
    }

    @Auditable(action = "UPDATE_PROGRAM", entity = "TrainingProgram")
    @PreAuthorize("hasPermission(null, 'program:update')")
    public TrainingProgramResponse update(UUID id, CreateProgramRequest request) {
        TrainingProgram program = findOrThrow(id);
        program.setTitle(request.title());
        program.setDescription(request.description());
        program.setLevel(request.level());
        program.setDurationWeeks(request.durationWeeks());
        program.setMaxStudents(request.maxStudents());
        program.setStartDate(request.startDate());
        program.setEndDate(request.endDate());
        return programMapper.toResponse(programRepository.save(program));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public TrainingProgramResponse findById(UUID id) {
        return programMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'student:read')")
    public PageResponse<TrainingProgramResponse> findAll(Pageable pageable) {
        return PageResponse.from(programRepository.findAll(pageable).map(programMapper::toResponse));
    }

    public TrainingProgram findOrThrow(UUID id) {
        return programRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.PROGRAM_NOT_FOUND, id));
    }
}
