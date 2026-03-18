package com.company.erp.modules.dashboard.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Revenue Chart Data Point
 */
@Data
@Schema(description = "Revenue chart data point")
public class RevenueDataPoint {
  @Schema(description = "Month")
  private String month;
  
  @Schema(description = "Revenus")
  private double revenus;
  
  @Schema(description = "Depenses")
  private double depenses;
  
  @Schema(description = "Benefice")
  private double benefice;
}

