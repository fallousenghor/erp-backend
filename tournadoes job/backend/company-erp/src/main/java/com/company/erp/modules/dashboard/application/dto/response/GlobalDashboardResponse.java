package com.company.erp.modules.dashboard.application.dto.response;

import java.math.BigDecimal;

public record GlobalDashboardResponse(
        // HR
        long totalEmployees,
        long activeEmployees,
        long pendingLeaveRequests,
        // Finance
        BigDecimal totalRevenue,
        BigDecimal totalPending,
        BigDecimal totalExpenses,
        long unpaidInvoices,
        // Inventory
        long totalAssets,
        long availableAssets,
        long assignedAssets,
        // Education
        long totalStudents,
        long activeEnrollments,
        long totalPrograms,
        // Organization
        long totalDepartments
) {}
