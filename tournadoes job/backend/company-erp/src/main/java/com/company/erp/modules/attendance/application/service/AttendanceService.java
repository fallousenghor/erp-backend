package com.company.erp.modules.attendance.application.service;

import com.company.erp.modules.attendance.application.dto.response.PresenceStatsResponse;
import com.company.erp.modules.attendance.application.dto.response.WeeklyPresenceResponse;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Attendance Service - Aggregate presence data
 * TODO: Implement all methods with actual repository queries
 */
@Service
@RequiredArgsConstructor
public class AttendanceService {

  public PresenceStatsResponse getPresenceStats() {
    // TODO: Query from AttendanceRepository
    return PresenceStatsResponse.builder()
      .days(Collections.emptyList())
      .build();
  }

  public WeeklyPresenceResponse getWeeklyPresence() {
    // TODO: Query weekly aggregation from AttendanceRepository
    return WeeklyPresenceResponse.builder()
      .days(Collections.emptyList())
      .build();
  }
}
