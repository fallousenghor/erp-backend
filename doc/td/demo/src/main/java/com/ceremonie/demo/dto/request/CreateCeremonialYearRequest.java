package com.ceremonie.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCeremonialYearRequest {
    
    @NotNull(message = "L'année est obligatoire")
    private Integer year;
    
    @NotBlank(message = "Le thème est obligatoire")
    private String theme;
    
    private String description;
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDate startDate;
    
    @NotNull(message = "La date de fin est obligatoire")
    private LocalDate endDate;
    
    private BigDecimal initialBudget;
}
