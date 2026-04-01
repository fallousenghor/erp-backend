package com.ceremonie.demo.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaterialRequest {
    
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    private String description;
    
    private String referenceNumber;
    
    @NotNull(message = "La quantité est obligatoire")
    @Positive(message = "La quantité doit être positive")
    private Integer quantity;
    
    private LocalDate purchaseDate;
    
    private String location;
    
    private String notes;
}
