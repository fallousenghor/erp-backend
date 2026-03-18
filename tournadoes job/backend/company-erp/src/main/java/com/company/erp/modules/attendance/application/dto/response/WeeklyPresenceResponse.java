package com.company.erp.modules.attendance.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import java.util.List;

/**
 * Frontend WeeklyPresenceData
 */
@Data
@Builder
@Schema(description = "Weekly presence data")
public class WeeklyPresenceResponse {
  private List<Day> days;

  @Data
  @Builder
  @Schema(description = "Week day stats")
  public static class Day {
    private String dayOfWeek;
    private Integer present;
    private Integer absent;
    private Integer late;
  }
}
