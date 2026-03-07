package com.company.erp.modules.hr.application.service;

import com.company.erp.modules.hr.application.dto.request.LeaveRequestDto;
import com.company.erp.modules.hr.application.dto.response.LeaveRequestResponse;
import com.company.erp.modules.hr.application.mapper.LeaveRequestMapper;
import com.company.erp.modules.hr.domain.event.LeaveApprovedEvent;
import com.company.erp.modules.hr.domain.event.LeaveRequestedEvent;
import com.company.erp.modules.hr.domain.model.Employee;
import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.domain.model.valueobject.LeaveType;
import com.company.erp.modules.hr.domain.repository.EmployeeRepository;
import com.company.erp.modules.hr.domain.repository.LeaveRequestRepository;
import com.company.erp.modules.hr.infrastructure.persistence.EmployeeJpaRepository;
import com.company.erp.modules.hr.infrastructure.persistence.LeaveRequestJpaRepository;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class LeaveService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeJpaRepository employeeJpaRepository;
    private final LeaveRequestJpaRepository leaveRequestJpaRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final DomainEventPublisher eventPublisher;

    // Default leave balance per employee (in days)
    private static final int DEFAULT_ANNUAL_LEAVE = 24;
    private static final int DEFAULT_SICK_LEAVE = 10;
    private static final int DEFAULT_MATERNITY_LEAVE = 14;
    private static final int DEFAULT_UNPAID_LEAVE = 30;
    private static final int DEFAULT_EXCEPTIONAL_LEAVE = 5;

    @Auditable(action = "SUBMIT_LEAVE_REQUEST", entity = "LeaveRequest")
    @PreAuthorize("hasPermission(null, 'leave:request')")
    public LeaveRequestResponse submit(UUID employeeId, LeaveRequestDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EMPLOYEE_NOT_FOUND, employeeId));

        // Domain rule: check leave balance
        employee.deductLeaveBalance(0); // will throw if balance validation fails inside entity

        LeaveRequest leaveRequest = LeaveRequest.create(
                employeeId,
                employee.getFullName(),
                dto.leaveType(),
                dto.startDate(),
                dto.endDate(),
                dto.reason()
        );

        // Validate balance against days requested
        employee.deductLeaveBalance(leaveRequest.getDaysRequested());
        employee.setOnLeave();

        leaveRequest = leaveRequestRepository.save(leaveRequest);
        employeeRepository.save(employee);

        eventPublisher.publish(new LeaveRequestedEvent(
                leaveRequest.getId(), employeeId,
                dto.startDate(), dto.endDate()));

        return leaveRequestMapper.toResponse(leaveRequest);
    }

    @Auditable(action = "APPROVE_LEAVE", entity = "LeaveRequest")
    @PreAuthorize("hasPermission(null, 'leave:approve')")
    public LeaveRequestResponse approve(UUID leaveRequestId) {
        LeaveRequest leaveRequest = findOrThrow(leaveRequestId);
        String approver = SecurityContextHolder.getContext().getAuthentication().getName();

        leaveRequest.approve(approver);
        leaveRequest = leaveRequestRepository.save(leaveRequest);

        eventPublisher.publish(new LeaveApprovedEvent(
                leaveRequest.getId(), leaveRequest.getEmployeeId(),
                leaveRequest.getDaysRequested(), approver));

        return leaveRequestMapper.toResponse(leaveRequest);
    }

    @Auditable(action = "REJECT_LEAVE", entity = "LeaveRequest")
    @PreAuthorize("hasPermission(null, 'leave:approve')")
    public LeaveRequestResponse reject(UUID leaveRequestId, String reason) {
        LeaveRequest leaveRequest = findOrThrow(leaveRequestId);
        String approver = SecurityContextHolder.getContext().getAuthentication().getName();

        leaveRequest.reject(approver, reason);

        // Restore employee balance and status
        employeeRepository.findById(leaveRequest.getEmployeeId()).ifPresent(emp -> {
            emp.restoreLeaveBalance(leaveRequest.getDaysRequested());
            emp.returnFromLeave();
            employeeRepository.save(emp);
        });

        return leaveRequestMapper.toResponse(leaveRequestRepository.save(leaveRequest));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'leave:request')")
    public PageResponse<LeaveRequestResponse> findByEmployee(UUID employeeId, Pageable pageable) {
        return PageResponse.from(leaveRequestRepository
                .findByEmployeeId(employeeId, pageable)
                .map(leaveRequestMapper::toResponse));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'leave:approve')")
    public PageResponse<LeaveRequestResponse> findPending(Pageable pageable) {
        return PageResponse.from(leaveRequestRepository
                .findByStatus(LeaveRequest.LeaveStatus.PENDING, pageable)
                .map(leaveRequestMapper::toResponse));
    }

    /**
     * Get all leave requests with pagination
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'leave:request')")
    public PageResponse<LeaveRequestResponse> findAll(Pageable pageable) {
        return PageResponse.from(leaveRequestRepository
                .findAll(pageable)
                .map(leaveRequestMapper::toResponse));
    }

    /**
     * Get leave distribution statistics by type (only approved leaves)
     * Returns count of leaves per leave type
     */
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Map<String, Integer> getStatsByType() {
        Map<String, Integer> stats = new HashMap<>();
        
        // Count approved leaves by type
        stats.put("ANNUAL", (int) leaveRequestJpaRepository.countByLeaveTypeAndStatus(
                LeaveType.ANNUAL, LeaveRequest.LeaveStatus.APPROVED));
        stats.put("SICK", (int) leaveRequestJpaRepository.countByLeaveTypeAndStatus(
                LeaveType.SICK, LeaveRequest.LeaveStatus.APPROVED));
        stats.put("MATERNITY", (int) leaveRequestJpaRepository.countByLeaveTypeAndStatus(
                LeaveType.MATERNITY, LeaveRequest.LeaveStatus.APPROVED));
        stats.put("UNPAID", (int) leaveRequestJpaRepository.countByLeaveTypeAndStatus(
                LeaveType.UNPAID, LeaveRequest.LeaveStatus.APPROVED));
        stats.put("EXCEPTIONAL", (int) leaveRequestJpaRepository.countByLeaveTypeAndStatus(
                LeaveType.EXCEPTIONAL, LeaveRequest.LeaveStatus.APPROVED));
        
        return stats;
    }

    /**
     * Get leave balances per employee
     * Returns balance for each leave type per employee
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(null, 'leave:request')")
    public List<Map<String, Object>> getBalancesByEmployee() {
        List<Employee> employees = employeeJpaRepository.findAll();
        
        return employees.stream().map(employee -> {
            Map<String, Object> balance = new HashMap<>();
            balance.put("employeeId", employee.getId().toString());
            balance.put("employeeName", employee.getFullName());
            balance.put("department", employee.getDepartmentId() != null ? employee.getDepartmentId().toString() : "N/A");
            
            // Calculate used days for each leave type
            int usedAnnual = getUsedDaysByType(employee.getId(), LeaveType.ANNUAL);
            int usedSick = getUsedDaysByType(employee.getId(), LeaveType.SICK);
            int usedMaternity = getUsedDaysByType(employee.getId(), LeaveType.MATERNITY);
            int usedUnpaid = getUsedDaysByType(employee.getId(), LeaveType.UNPAID);
            int usedExceptional = getUsedDaysByType(employee.getId(), LeaveType.EXCEPTIONAL);
            
            // Build balances map
            Map<String, Map<String, Integer>> balances = new HashMap<>();
            
            balances.put("annuel", Map.of(
                "total", DEFAULT_ANNUAL_LEAVE,
                "used", usedAnnual,
                "remaining", DEFAULT_ANNUAL_LEAVE - usedAnnual
            ));
            balances.put("maladie", Map.of(
                "total", DEFAULT_SICK_LEAVE,
                "used", usedSick,
                "remaining", DEFAULT_SICK_LEAVE - usedSick
            ));
            balances.put("maternite", Map.of(
                "total", DEFAULT_MATERNITY_LEAVE,
                "used", usedMaternity,
                "remaining", DEFAULT_MATERNITY_LEAVE - usedMaternity
            ));
            balances.put("sans_solde", Map.of(
                "total", DEFAULT_UNPAID_LEAVE,
                "used", usedUnpaid,
                "remaining", DEFAULT_UNPAID_LEAVE - usedUnpaid
            ));
            balances.put("exceptionnel", Map.of(
                "total", DEFAULT_EXCEPTIONAL_LEAVE,
                "used", usedExceptional,
                "remaining", DEFAULT_EXCEPTIONAL_LEAVE - usedExceptional
            ));
            
            balance.put("balances", balances);
            return balance;
        }).toList();
    }

    /**
     * Helper method to get used days for a specific leave type
     */
    private int getUsedDaysByType(UUID employeeId, LeaveType leaveType) {
        // Get all approved leave requests for this employee and type
        List<LeaveRequest> approvedLeaves = leaveRequestJpaRepository
                .findByEmployeeId(employeeId, Pageable.unpaged())
                .getContent()
                .stream()
                .filter(l -> l.getLeaveType() == leaveType && l.getStatus() == LeaveRequest.LeaveStatus.APPROVED)
                .toList();
        
        return approvedLeaves.stream().mapToInt(LeaveRequest::getDaysRequested).sum();
    }

    private LeaveRequest findOrThrow(UUID id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LEAVE_REQUEST_NOT_FOUND, id));
    }
}
