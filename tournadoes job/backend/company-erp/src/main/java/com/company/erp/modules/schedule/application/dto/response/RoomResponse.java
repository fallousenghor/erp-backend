package com.company.erp.modules.schedule.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * Room Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {
    private UUID id;
    private String name;
    private String description;
    private Integer capacity;
    private String location;
    private String equipments;
}
