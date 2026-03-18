package com.company.erp.modules.schedule.presentation.controller;

import com.company.erp.modules.schedule.application.dto.response.RoomResponse;
import com.company.erp.modules.schedule.application.dto.response.ScheduleResponse;
import com.company.erp.modules.schedule.application.service.ScheduleService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Schedule Controller - Handle schedules and rooms management
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Schedule", description = "Schedule & Room management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleService scheduleService;

  @GetMapping("/schedules")
  @Operation(summary = "Get all schedules")
  public ResponseEntity<ApiResponse<PageResponse<ScheduleResponse>>> getSchedules(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Pageable pageable = PageRequest.of(page, size);
    return ResponseEntity.ok(ApiResponse.success(scheduleService.getAllSchedules(pageable)));
  }

  @GetMapping("/rooms")
  @Operation(summary = "Get all rooms")
  public ResponseEntity<ApiResponse<List<RoomResponse>>> getRooms() {
    return ResponseEntity.ok(ApiResponse.success(scheduleService.getAllRooms()));
  }
}
