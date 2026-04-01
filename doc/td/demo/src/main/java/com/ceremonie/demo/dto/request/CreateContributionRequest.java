package com.ceremonie.demo.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContributionRequest {
    
    @NotNull(message = "Le membre est obligatoire")
    private Long memberId;
    
    @NotNull(message = "L'année cérémoniale est obligatoire")
    private Long ceremonialYearId;
    
    @NotNull(message = "Le montant attendu est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal expectedAmount;
    
    private LocalDate dueDate;
    
    private String notes;
}
