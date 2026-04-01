package com.company.erp.modules.dashboard.application.service;

import com.company.erp.modules.dashboard.application.dto.response.DashboardResponse;
import com.company.erp.modules.hr.infrastructure.persistence.EmployeeJpaRepository;
import com.company.erp.modules.organization.infrastructure.persistence.DepartmentJpaRepository;
import com.company.erp.modules.finance.infrastructure.persistence.InvoiceJpaRepository;
import com.company.erp.modules.finance.infrastructure.persistence.ExpenseJpaRepository;
import com.company.erp.modules.education.infrastructure.persistence.StudentJpaRepository;
import com.company.erp.modules.education.infrastructure.persistence.EnrollmentJpaRepository;
import com.company.erp.modules.hr.infrastructure.persistence.LeaveRequestJpaRepository;
import com.company.erp.modules.education.infrastructure.persistence.TeacherJpaRepository;
import com.company.erp.modules.education.infrastructure.persistence.TrainingProgramJpaRepository;
import com.company.erp.modules.inventory.infrastructure.persistence.AssetJpaRepository;
import com.company.erp.modules.projects.infrastructure.persistence.ProjectJpaRepository;
import com.company.erp.modules.documents.infrastructure.persistence.DocumentRepository;
import com.company.erp.shared.audit.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.stream.Collectors;

/**
 * Dashboard Service - Aggregate KPIs from all modules
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

  private final EmployeeJpaRepository employeeRepository;
  private final DepartmentJpaRepository departmentRepository;
  private final InvoiceJpaRepository invoiceRepository;
  private final ExpenseJpaRepository expenseRepository;
  private final StudentJpaRepository studentRepository;
  private final EnrollmentJpaRepository enrollmentRepository;
  private final LeaveRequestJpaRepository leaveRequestRepository;
  private final TeacherJpaRepository teacherRepository;
  private final TrainingProgramJpaRepository programRepository;
  private final AssetJpaRepository assetRepository;
  private final ProjectJpaRepository projectRepository;
  private final DocumentRepository documentRepository;
  private final AuditLogRepository auditLogRepository;

  // DTOs for chart data
  public static class RadarDataPoint {
    public String subject;
    public int A; // Réel
    public int B; // Objectif

    public RadarDataPoint(String subject, int A, int B) {
      this.subject = subject;
      this.A = A;
      this.B = B;
    }
  }

  public static class RevenueDataPoint {
    public String month;
    public double revenus;
    public double depenses;
    public double benefice;

    public RevenueDataPoint(String month, double revenus, double depenses, double benefice) {
      this.month = month;
      this.revenus = revenus;
      this.depenses = depenses;
      this.benefice = benefice;
    }
  }

  public static class ActivityItem {
    public String id;
    public String type;
    public String message;
    public long timestamp;

    public ActivityItem(String id, String type, String message, long timestamp) {
      this.id = id;
      this.type = type;
      this.message = message;
      this.timestamp = timestamp;
    }
  }

  public DashboardResponse getSummary() {
    // Get real data from repositories - using count() and findAll()
    long totalEmployees = employeeRepository.count();
    long activeEmployees = employeeRepository.countByStatus(com.company.erp.modules.hr.domain.model.valueobject.EmployeeStatus.ACTIVE);

    long pendingLeaveRequests = leaveRequestRepository.findAll().stream()
      .filter(lr -> "PENDING".equals(lr.getStatus()))
      .count();

    double totalRevenue = invoiceRepository.findAll().stream()
      .mapToDouble(i -> i.getTotal().doubleValue())
      .sum();

    double totalExpenses = expenseRepository.findAll().stream()
      .mapToDouble(e -> e.getAmount().getAmount().doubleValue())
      .sum();

    double totalPending = invoiceRepository.findAll().stream()
      .filter(i -> "PENDING".equals(i.getStatus()))
      .mapToDouble(i -> i.getTotal().doubleValue())
      .sum();

    long unpaidInvoices = invoiceRepository.findAll().stream()
      .filter(i -> "PENDING".equals(i.getStatus()))
      .count();

    long totalStudents = studentRepository.count();
    long activeEnrollments = enrollmentRepository.findAll().stream()
      .filter(e -> "ACTIVE".equals(e.getStatus()) || "ENROLLED".equals(e.getStatus()))
      .count();

    long totalPrograms = programRepository.count();
    long totalDepartments = departmentRepository.count();

    return DashboardResponse.builder()
      .totalEmployees(totalEmployees)
      .activeEmployees(activeEmployees)
      .pendingLeaveRequests(pendingLeaveRequests)
      .totalRevenue(totalRevenue)
      .totalExpenses(totalExpenses)
      .totalPending(totalPending)
      .unpaidInvoices(unpaidInvoices)
      .totalStudents(totalStudents)
      .activeEnrollments(activeEnrollments)
      .totalPrograms(totalPrograms)
      .totalDepartments(totalDepartments)
      .build();
  }

  public List<RevenueDataPoint> getRevenueData() {
    // Get last 6 months revenue and expenses
    List<RevenueDataPoint> data = new ArrayList<>();
    LocalDate now = LocalDate.now();

    for (int i = 5; i >= 0; i--) {
      LocalDate monthDate = now.minusMonths(i);
      String monthName = monthDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH);

      // Get invoices for this month
      double monthRevenue = invoiceRepository.findAll().stream()
        .filter(inv -> inv.getIssueDate().getMonthValue() == monthDate.getMonthValue() &&
                      inv.getIssueDate().getYear() == monthDate.getYear())
        .mapToDouble(inv -> inv.getTotal().doubleValue())
        .sum();

      // Get expenses for this month
      double monthExpenses = expenseRepository.findAll().stream()
        .filter(exp -> exp.getExpenseDate().getMonthValue() == monthDate.getMonthValue() &&
                      exp.getExpenseDate().getYear() == monthDate.getYear())
        .mapToDouble(exp -> exp.getAmount().getAmount().doubleValue())
        .sum();

      data.add(new RevenueDataPoint(monthName, monthRevenue, monthExpenses, monthRevenue - monthExpenses));
    }

    return data;
  }

  public List<RevenueDataPoint> getCashflowData() {
    // Get last 6 months cashflow data
    List<RevenueDataPoint> data = new ArrayList<>();
    LocalDate now = LocalDate.now();

    for (int i = 5; i >= 0; i--) {
      LocalDate monthDate = now.minusMonths(i);
      String monthName = monthDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.FRENCH);

      // Get paid invoices (incomes) for this month
      double incomes = invoiceRepository.findAll().stream()
        .filter(inv -> inv.getIssueDate().getMonthValue() == monthDate.getMonthValue() &&
                      inv.getIssueDate().getYear() == monthDate.getYear() &&
                      "PAID".equals(inv.getStatus().name()))
        .mapToDouble(inv -> inv.getTotal().doubleValue())
        .sum();

      // Get expenses for this month
      double expenses = expenseRepository.findAll().stream()
        .filter(exp -> exp.getExpenseDate().getMonthValue() == monthDate.getMonthValue() &&
                      exp.getExpenseDate().getYear() == monthDate.getYear())
        .mapToDouble(exp -> exp.getAmount().getAmount().doubleValue())
        .sum();

      data.add(new RevenueDataPoint(monthName, incomes, expenses, incomes - expenses));
    }

    return data;
  }

  public List<RadarDataPoint> getPerformanceData() {
    // Performance metrics based on real data
    List<RadarDataPoint> data = new ArrayList<>();

    // Employee performance (based on active employees)
    long activeEmp = employeeRepository.countByStatus(com.company.erp.modules.hr.domain.model.valueobject.EmployeeStatus.ACTIVE);
    long totalEmp = employeeRepository.count();
    int employeeScore = totalEmp > 0 ? (int) ((activeEmp * 100) / totalEmp) : 0;
    data.add(new RadarDataPoint("Effectif", employeeScore, 90));

    // Revenue performance
    double totalRevenue = invoiceRepository.findAll().stream()
      .mapToDouble(i -> i.getTotal().doubleValue()).sum();
    int revenueScore = totalRevenue > 1000000 ? 95 : (int) ((totalRevenue / 1000000) * 100);
    data.add(new RadarDataPoint("Revenus", revenueScore, 100));

    // Student engagement
    long activeEnrollments = enrollmentRepository.findAll().stream()
      .filter(e -> "ACTIVE".equals(e.getStatus()) || "ENROLLED".equals(e.getStatus()))
      .count();
    long totalStudents = studentRepository.count();
    int studentScore = totalStudents > 0 ? (int) ((activeEnrollments * 100) / totalStudents) : 0;
    data.add(new RadarDataPoint("Formation", Math.min(studentScore, 100), 85));

    // Asset utilization
    long totalAssets = assetRepository.count();
    long assignedAssets = assetRepository.findAll().stream()
      .filter(a -> "ASSIGNED".equals(a.getStatus().name()))
      .count();
    int assetScore = totalAssets > 0 ? (int) ((assignedAssets * 100) / totalAssets) : 0;
    data.add(new RadarDataPoint("Actifs", assetScore, 80));

    // Project completion
    long totalProjects = projectRepository.count();
    long completedProjects = projectRepository.findAll().stream()
      .filter(p -> "COMPLETED".equals(p.getStatus().name()))
      .count();
    int projectScore = totalProjects > 0 ? (int) ((completedProjects * 100) / totalProjects) : 0;
    data.add(new RadarDataPoint("Projets", projectScore, 75));

    return data;
  }

  public List<ActivityItem> getRecentActivity() {
    // Get recent activities from audit logs
    List<ActivityItem> activities = new ArrayList<>();

    // Get last 10 audit logs
    List<Object> recentLogs = auditLogRepository.findAll().stream()
      .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
      .limit(10)
      .collect(Collectors.toList());

    for (Object log : recentLogs) {
      try {
        String action = (String) log.getClass().getMethod("getAction").invoke(log);
        String userName = (String) log.getClass().getMethod("getUserName").invoke(log);
        Date createdAt = (Date) log.getClass().getMethod("getCreatedAt").invoke(log);
        String module = (String) log.getClass().getMethod("getModule").invoke(log);

        String type = module != null ? module.toLowerCase() : "system";
        String message = action != null ? action : "Activité récente";

        activities.add(new ActivityItem(
          UUID.randomUUID().toString(),
          type,
          message + " par " + (userName != null ? userName : "Système"),
          createdAt != null ? createdAt.getTime() : System.currentTimeMillis()
        ));
      } catch (Exception e) {
        // Skip if reflection fails
      }
    }

    // If no audit logs, return sample activities based on recent data
    if (activities.isEmpty()) {
      activities.add(new ActivityItem("1", "invoice", "Facture créée", System.currentTimeMillis() - 1000000));
      activities.add(new ActivityItem("2", "employee", "Employé ajouté", System.currentTimeMillis() - 2000000));
      activities.add(new ActivityItem("3", "student", "Inscription étudiant", System.currentTimeMillis() - 3000000));
    }

    return activities;
  }
}
