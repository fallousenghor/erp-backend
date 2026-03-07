package com.company.erp.modules.dashboard.application.dto.response;

/**
 * DTO for department performance radar data
 */
public record PerformanceRadarDataResponse(
        String subject,
        double A, // Actual value
        double B  // Target value
) {
    public static PerformanceRadarDataResponse of(String subject, double actual, double target) {
        return new PerformanceRadarDataResponse(subject, actual, target);
    }
}

