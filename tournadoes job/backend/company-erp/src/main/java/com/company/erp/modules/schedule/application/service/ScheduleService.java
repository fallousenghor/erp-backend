package com.company.erp.modules.schedule.application.service;

import com.company.erp.modules.schedule.application.dto.response.RoomResponse;
import com.company.erp.modules.schedule.application.dto.response.ScheduleResponse;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

/**
 * Schedule Service - TODO: Implement with real repository queries
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

  public PageResponse<ScheduleResponse> getAllSchedules(Pageable pageable) {
    // TODO: Query from ScheduleRepository
    return PageResponse.empty();
  }

  public List<RoomResponse> getAllRooms() {
    // TODO: Query from RoomRepository
    return Collections.emptyList();
  }
}
