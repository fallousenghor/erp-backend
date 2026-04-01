package com.ceremonie.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.ceremonie.demo.enums.EventType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEventRequest {
    
    @NotNull(message = "L'année cérémoniale est obligatoire")
    private Long ceremonialYearId;
    
    @NotBlank(message = "Le titre est obligatoire")
    private String title;
    
    private String description;
    
    @NotNull(message = "Le type d'événement est obligatoire")
    private EventType type;
    
    @NotNull(message = "La date de début est obligatoire")
    private LocalDateTime startDate;
    
    private LocalDateTime endDate;
    
    private String location;
    
    private String address;
    
    private List<Long> participantIds;
    
    private LocalDateTime reminderDate;
    
    private String notes;
}
