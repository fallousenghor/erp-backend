package com.company.erp.modules.dashboard.application.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for recent activity feed data
 */
public record ActivityResponse(
        String id,
        String type,
        String title,
        String description,
        String user,
        LocalDateTime timestamp,
        String icon,
        String color
) {
    public static ActivityResponse of(String id, String type, String title, String description, 
                                       String user, LocalDateTime timestamp, String icon, String color) {
        return new ActivityResponse(id, type, title, description, user, timestamp, icon, color);
    }
}

