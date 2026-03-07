package com.company.erp.modules.dashboard.presentation.controller;

import com.company.erp.modules.dashboard.application.dto.response.*;
import com.company.erp.modules.dashboard.application.service.DashboardService;
import com.company.erp.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/dashboard")
@Tag(name = "Dashboard", description = "Global ERP statistics and KPIs")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get global ERP dashboard — all modules KPIs")
    public ResponseEntity<ApiResponse<GlobalDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getGlobalDashboard()));
    }

    @GetMapping("/revenue")
    @Operation(summary = "Get monthly revenue data for charts")
    public ResponseEntity<ApiResponse<List<RevenueDataResponse>>> getRevenueData() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getRevenueData()));
    }

    @GetMapping("/cashflow")
    @Operation(summary = "Get cash flow data for charts")
    public ResponseEntity<ApiResponse<List<CashFlowDataResponse>>> getCashFlowData() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getCashFlowData()));
    }

    @GetMapping("/performance")
    @Operation(summary = "Get department performance radar data")
    public ResponseEntity<ApiResponse<List<PerformanceRadarDataResponse>>> getPerformanceRadarData() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getPerformanceRadarData()));
    }

    @GetMapping("/activity")
    @Operation(summary = "Get recent activity feed")
    public ResponseEntity<ApiResponse<List<ActivityResponse>>> getRecentActivity() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getRecentActivity()));
    }
}
