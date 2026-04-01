package com.ceremonie.demo.dto.request;

import com.ceremonie.demo.enums.MediaType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMediaRequest {
    
    @NotNull(message = "L'année cérémoniale est obligatoire")
    private Long ceremonialYearId;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    private String description;
    
    @NotNull(message = "Le type de média est obligatoire")
    private MediaType type;
    
    private String tags;
}
