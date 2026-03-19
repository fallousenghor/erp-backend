package com.company.erp.modules.education.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Create or update module grade")
public class CreateGradeRequest {
    
    @NotNull
    @Schema(description = "Student ID", required = true)
    private UUID studentId;
    
    @NotNull
    @Schema(description = "Module ID", required = true)
    private UUID moduleId;
    
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("20.0")
    @Schema(description = "Grade score", example = "15.5", required = true)
    private BigDecimal score;
    
    @Builder.Default
    @Schema(description = "Max score (default 20)")
    private BigDecimal maxScore = BigDecimal.valueOf(20);
    
    @Schema(description = "Grade comments")
    private String comments;
    
    @Builder.Default
    @Schema(description = "Recorded date")
    private LocalDate recordedDate = LocalDate.now();
}

