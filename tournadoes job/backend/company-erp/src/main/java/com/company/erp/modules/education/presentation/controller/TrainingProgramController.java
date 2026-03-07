package com.company.erp.modules.education.presentation.controller;

import com.company.erp.modules.education.application.dto.request.CreateProgramRequest;
import com.company.erp.modules.education.application.dto.response.TrainingProgramResponse;
import com.company.erp.modules.education.application.service.TrainingProgramService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/programs")
@Tag(name = "Training Programs", description = "Education — Program management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TrainingProgramController {

    private final TrainingProgramService programService;

    @PostMapping
    @Operation(summary = "Create a new training program")
    public ResponseEntity<ApiResponse<TrainingProgramResponse>> create(
            @Valid @RequestBody CreateProgramRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(programService.create(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainingProgramResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(programService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TrainingProgramResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                programService.findAll(PageRequest.of(page, size, Sort.by("title")))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a training program")
    public ResponseEntity<ApiResponse<TrainingProgramResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateProgramRequest request) {
        return ResponseEntity.ok(ApiResponse.success(programService.update(id, request)));
    }
}
