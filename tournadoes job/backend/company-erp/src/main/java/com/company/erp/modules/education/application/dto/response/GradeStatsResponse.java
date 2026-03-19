package com.company.erp.modules.education.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Global grade statistics")
public class GradeStatsResponse {
    
    @Schema(description = "Overall average grade")
    private BigDecimal average;
    
    @Schema(description = "Highest grade")
    private BigDecimal highest;
    
    @Schema(description = "Lowest grade")
    private BigDecimal lowest;
    
    @Schema(description = "Pass rate percentage (≥10/20)", example = "75.5")
    private BigDecimal passRate;
    
    @Schema(description = "Total number of students with grades")
    private Long totalStudents;
}

