package com.company.erp.modules.education.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Student with average grade summary")
public class StudentAverageResponse {
    
    @Schema(description = "Student ID")
    private UUID id;
    
    @Schema(description = "First name")
    private String firstName;
    
    @Schema(description = "Last name")
    private String lastName;
    
    @Schema(description = "Email")
    private String email;
    
    @Schema(description = "Program title")
    private String program;
    
    @Schema(description = "Avatar URL", example = "/api/v1/students/{id}/avatar")
    private String avatar;
    
    @Schema(description = "Weighted average grade")
    private BigDecimal average;
    
    @Schema(description = "Number of module grades")
    private Long gradesCount;
}

