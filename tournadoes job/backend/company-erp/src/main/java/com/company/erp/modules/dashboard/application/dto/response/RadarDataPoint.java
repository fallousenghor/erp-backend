package com.company.erp.modules.dashboard.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Radar Chart Data Point
 */
@Data
@Schema(description = "Radar chart data point")
public class RadarDataPoint {
  @Schema(description = "Metric name")
  private String subject;
  
  @Schema(description = "Actual value")
  private int A;
  
  @Schema(description = "Target value")
  private int B;
}

