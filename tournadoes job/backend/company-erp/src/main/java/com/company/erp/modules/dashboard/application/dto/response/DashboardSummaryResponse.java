package com.company.erp.modules.dashboard.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Dashboard Summary Response - Frontend Dashboard KPIs
 */
@Data
@Builder
@Schema(description = "Global Dashboard Summary")
public class DashboardSummaryResponse {
  
  @Schema(description = "Total employees")
  private Long totalEmployees;
  
  @Schema(description = "Active employees")
  private Long activeEmployees;
  
  @Schema(description = "Pending leave requests")
  private Long pendingLeaveRequests;
  
  @Schema(description = "Total revenue")
  private Double totalRevenue;
  
  @Schema(description = "Total pending invoices")
  private Long totalPending;
  
  @Schema(description = "Total expenses")
  private Double totalExpenses;
  
  @Schema(description = "Unpaid invoices count")
  private Long unpaidInvoices;
}
