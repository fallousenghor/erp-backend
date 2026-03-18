package com.company.erp.modules.education.presentation.controller;

import com.company.erp.modules.education.application.dto.response.TeacherResponse;
import com.company.erp.modules.education.application.service.TeacherService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/teachers")
@Tag(name = "Teachers", description = "Education — Teacher management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(teacherService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TeacherResponse>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                teacherService.findAll(PageRequest.of(page, size))));
    }
}
