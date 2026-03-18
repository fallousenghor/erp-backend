package com.company.erp.modules.dashboard.presentation.controller;

import com.company.erp.modules.dashboard.application.dto.response.DashboardResponse;
import com.company.erp.modules.dashboard.application.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Dashboard Controller - Global KPIs and analytics
 * Frontend calls: /v1/dashboard, /v1/dashboard/revenue, etc.
 */
@Tag(name = "Dashboard", description = "Global KPIs & Analytics")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardController {

  private final DashboardService dashboardService;

  @Operation(summary = "Global dashboard summary")
  @GetMapping
  public ResponseEntity<DashboardResponse> getDashboard() {
    return ResponseEntity.ok(dashboardService.getSummary());
  }

  @Operation(summary = "Revenue data for chart")
  @GetMapping("/revenue")
  public ResponseEntity<?> getRevenueData() {
    return ResponseEntity.ok(dashboardService.getRevenueData());
  }

  @Operation(summary = "Cashflow data for chart")
  @GetMapping("/cashflow")
  public ResponseEntity<?> getCashflowData() {
    return ResponseEntity.ok(dashboardService.getCashflowData());
  }

  @Operation(summary = "Performance radar data")
  @GetMapping("/performance")
  public ResponseEntity<?> getPerformanceData() {
    return ResponseEntity.ok(dashboardService.getPerformanceData());
  }

  @Operation(summary = "Recent activity feed")
  @GetMapping("/activity")
  public ResponseEntity<?> getRecentActivity() {
    return ResponseEntity.ok(dashboardService.getRecentActivity());
  }
}
