package com.company.erp.modules.dashboard.application.service;

import com.company.erp.modules.dashboard.application.dto.response.*;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import com.company.erp.modules.education.infrastructure.persistence.EnrollmentJpaRepository;
import com.company.erp.modules.education.infrastructure.persistence.StudentJpaRepository;
import com.company.erp.modules.education.infrastructure.persistence.TrainingProgramJpaRepository;
import com.company.erp.modules.finance.domain.model.Expense;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import com.company.erp.modules.finance.infrastructure.persistence.ExpenseJpaRepository;
import com.company.erp.modules.finance.infrastructure.persistence.InvoiceJpaRepository;
import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.domain.model.valueobject.EmployeeStatus;
import com.company.erp.modules.hr.infrastructure.persistence.EmployeeJpaRepository;
import com.company.erp.modules.hr.infrastructure.persistence.LeaveRequestJpaRepository;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import com.company.erp.modules.inventory.infrastructure.persistence.AssetJpaRepository;
import com.company.erp.modules.organization.infrastructure.persistence.DepartmentJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final EmployeeJpaRepository employeeRepo;
    private final LeaveRequestJpaRepository leaveRepo;
    private final InvoiceJpaRepository invoiceRepo;
    private final ExpenseJpaRepository expenseRepo;
    private final AssetJpaRepository assetRepo;
    private final DepartmentJpaRepository departmentRepo;
    private final StudentJpaRepository studentRepo;
    private final EnrollmentJpaRepository enrollmentRepo;
    private final TrainingProgramJpaRepository programRepo;

    @PreAuthorize("hasPermission(null, 'dashboard:view')")
    public GlobalDashboardResponse getGlobalDashboard() {
        // HR
        long totalEmployees  = employeeRepo.count();
        long activeEmployees = employeeRepo.countByStatus(EmployeeStatus.ACTIVE);
        long pendingLeaves   = leaveRepo.countByStatus(LeaveRequest.LeaveStatus.PENDING);

        // Finance
        BigDecimal revenue    = nvl(invoiceRepo.sumTotalByStatus(InvoiceStatus.PAID));
        BigDecimal pending    = nvl(invoiceRepo.sumTotalByStatus(InvoiceStatus.SENT));
        BigDecimal expenses   = nvl(expenseRepo.sumAmountByStatus(Expense.ExpenseStatus.PAID));
        long unpaidInvoices   = invoiceRepo.countByStatus(InvoiceStatus.SENT);

        // Inventory
        long totalAssets     = assetRepo.count();
        long availableAssets = assetRepo.countByStatus(AssetStatus.AVAILABLE);
        long assignedAssets  = assetRepo.countByStatus(AssetStatus.ASSIGNED);

        // Org
        long totalDepts      = departmentRepo.countByDeletedFalse();

        // Education
        long totalStudents      = studentRepo.count();
        long activeEnrollments  = enrollmentRepo.countByStatus(EnrollmentStatus.ACTIVE);
        long totalPrograms      = programRepo.count();

        return new GlobalDashboardResponse(
                totalEmployees, activeEmployees, pendingLeaves,
                revenue, pending, expenses, unpaidInvoices,
                totalAssets, availableAssets, assignedAssets,
                totalStudents, activeEnrollments, totalPrograms,
                totalDepts
        );
    }

    private BigDecimal nvl(BigDecimal val) {
        return val != null ? val : BigDecimal.ZERO;
    }

    /**
     * Get monthly revenue data for charts
     */
    @PreAuthorize("hasPermission(null, 'dashboard:view')")
    public List<RevenueDataResponse> getRevenueData() {
        // For now, return mock data for the last 12 months
        // In production, this would query invoices grouped by month
        List<RevenueDataResponse> data = new ArrayList<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        BigDecimal[] revenues = {
            new BigDecimal("45000"), new BigDecimal("52000"), new BigDecimal("48000"),
            new BigDecimal("61000"), new BigDecimal("55000"), new BigDecimal("67000"),
            new BigDecimal("72000"), new BigDecimal("68000"), new BigDecimal("75000"),
            new BigDecimal("81000"), new BigDecimal("78000"), new BigDecimal("95000")
        };
        BigDecimal[] expenses = {
            new BigDecimal("32000"), new BigDecimal("35000"), new BigDecimal("31000"),
            new BigDecimal("40000"), new BigDecimal("38000"), new BigDecimal("42000"),
            new BigDecimal("45000"), new BigDecimal("43000"), new BigDecimal("48000"),
            new BigDecimal("52000"), new BigDecimal("49000"), new BigDecimal("58000")
        };
        
        for (int i = 0; i < 12; i++) {
            data.add(RevenueDataResponse.of(months[i], revenues[i], expenses[i]));
        }
        return data;
    }

    /**
     * Get cash flow data for charts
     */
    @PreAuthorize("hasPermission(null, 'dashboard:view')")
    public List<CashFlowDataResponse> getCashFlowData() {
        // For now, return mock data for the last 12 months
        // In production, this would query invoices (incomes) and expenses grouped by month
        List<CashFlowDataResponse> data = new ArrayList<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        BigDecimal[] incomes = {
            new BigDecimal("45000"), new BigDecimal("52000"), new BigDecimal("48000"),
            new BigDecimal("61000"), new BigDecimal("55000"), new BigDecimal("67000"),
            new BigDecimal("72000"), new BigDecimal("68000"), new BigDecimal("75000"),
            new BigDecimal("81000"), new BigDecimal("78000"), new BigDecimal("95000")
        };
        BigDecimal[] expenses = {
            new BigDecimal("32000"), new BigDecimal("35000"), new BigDecimal("31000"),
            new BigDecimal("40000"), new BigDecimal("38000"), new BigDecimal("42000"),
            new BigDecimal("45000"), new BigDecimal("43000"), new BigDecimal("48000"),
            new BigDecimal("52000"), new BigDecimal("49000"), new BigDecimal("58000")
        };
        
        for (int i = 0; i < 12; i++) {
            data.add(CashFlowDataResponse.of(months[i], incomes[i], expenses[i]));
        }
        return data;
    }

    /**
     * Get department performance radar data
     */
    @PreAuthorize("hasPermission(null, 'dashboard:view')")
    public List<PerformanceRadarDataResponse> getPerformanceRadarData() {
        // For now, return mock data for departments
        // In production, this would calculate actual performance metrics per department
        List<PerformanceRadarDataResponse> data = new ArrayList<>();
        data.add(PerformanceRadarDataResponse.of("Tech", 85, 90));
        data.add(PerformanceRadarDataResponse.of("RH", 78, 80));
        data.add(PerformanceRadarDataResponse.of("Finance", 92, 85));
        data.add(PerformanceRadarDataResponse.of("Ventes", 88, 90));
        data.add(PerformanceRadarDataResponse.of("Formation", 95, 85));
        return data;
    }

    /**
     * Get recent activity feed
     */
    @PreAuthorize("hasPermission(null, 'dashboard:view')")
    public List<ActivityResponse> getRecentActivity() {
        // For now, return mock activity data
        // In production, this would aggregate recent events from various modules
        List<ActivityResponse> activities = new ArrayList<>();
        
        activities.add(ActivityResponse.of(
            "1", "invoice", "Nouvelle facture créée", 
            "Facture #INV-2024-001 pour Société ABC", 
            "Admin", LocalDateTime.now().minusHours(1), "📄", "#6490ff"
        ));
        activities.add(ActivityResponse.of(
            "2", "employee", "Nouvel employé", 
            "Jean Dupont ajouté au département Tech", 
            "RH", LocalDateTime.now().minusHours(3), "👤", "#3ecf8e"
        ));
        activities.add(ActivityResponse.of(
            "3", "enrollment", "Nouvelle inscription", 
            "Marie Martin inscrite au programme Python", 
            "Formation", LocalDateTime.now().minusHours(5), "📚", "#a78bfa"
        ));
        activities.add(ActivityResponse.of(
            "4", "leave", "Demande de congés", 
            "Sophie Bernard a demandé des congés", 
            "RH", LocalDateTime.now().minusDays(1), "🏖️", "#fb923c"
        ));
        activities.add(ActivityResponse.of(
            "5", "invoice", "Facture payée", 
            "Facture #INV-2024-045 marquée comme payée", 
            "Finance", LocalDateTime.now().minusDays(1), "✅", "#2dd4bf"
        ));
        
        return activities;
    }
}
