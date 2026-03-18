package com.company.erp.modules.dashboard.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * Dashboard Response - Matches Frontend DashboardResponse interface
 */
@Data
@Builder
@Schema(description = "Complete Dashboard Response for Frontend")
public class DashboardResponse {
  
  // HR
  @Schema(description = "Total employees")
  private Long totalEmployees;
  
  @Schema(description = "Active employees")
  private Long activeEmployees;
  
  @Schema(description = "Pending leave requests")
  private Long pendingLeaveRequests;
  
  // Finance
  @Schema(description = "Total revenue")
  private Double totalRevenue;
  
  @Schema(description = "Total pending")
  private Double totalPending;
  
  @Schema(description = "Total expenses")
  private Double totalExpenses;
  
  @Schema(description = "Unpaid invoices")
  private Long unpaidInvoices;
  
  // Education
  @Schema(description = "Total students")
  private Long totalStudents;
  
  @Schema(description = "Active enrollments")
  private Long activeEnrollments;
  
  @Schema(description = "Total programs")
  private Long totalPrograms;
  
  // Organization
  @Schema(description = "Total departments")
  private Long totalDepartments;
}

