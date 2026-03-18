package com.company.erp.modules.attendance.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Frontend PresenceDayData
 */
@Data
@Builder
@Schema(description = "Daily presence stats")
public class PresenceStatsResponse {
  private List<PresenceDay> days;

  @Data
  @Builder
  @Schema(description = "Day stats")
  public static class PresenceDay {
    private String jour;
    private Integer presents;
    private Integer absents;
    private Integer retards;
  }
}
