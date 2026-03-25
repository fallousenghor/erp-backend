package com.company.erp.modules.hr.application.service;

import com.company.erp.modules.hr.application.dto.request.CreateLeaveRequest;
import com.company.erp.modules.hr.application.dto.response.LeaveRequestResponse;
import com.company.erp.modules.hr.application.dto.response.LeaveStatsResponse;
import com.company.erp.modules.hr.domain.model.LeaveRequest;
import com.company.erp.modules.hr.domain.repository.LeaveRepository;
import com.company.erp.modules.hr.application.mapper.LeaveMapper;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import com.company.erp.shared.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class LeaveService {

  private final LeaveRepository leaveRepository;
  private final LeaveMapper leaveMapper;

  @PreAuthorize("hasPermission(null, 'leave:request')")
  public LeaveRequestResponse create(CreateLeaveRequest request) {
    long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;
    if (days <= 0) {
      throw new BusinessException(ErrorCode.VALIDATION_ERROR, "End date must be after start date");
    }

    var leaveRequest = LeaveRequest.builder()
        .employeeId(request.getEmployeeId())
        .leaveType(request.getLeaveType())
        .startDate(request.getStartDate())
        .endDate(request.getEndDate())
        .daysRequested((int) days)
        .reason(request.getReason())
        .status("PENDING")
        .build();

    var saved = leaveRepository.save(leaveRequest);
    log.info("Leave request created: id={} employee={} type={} days={}", 
        saved.getId(), request.getEmployeeId(), request.getLeaveType(), days);
    
    return leaveMapper.toResponse(saved);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public LeaveRequestResponse findById(UUID id) {
    var leave = leaveRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LEAVE_NOT_FOUND, id));
    return leaveMapper.toResponse(leave);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public PageResponse<LeaveRequestResponse> findAll(
      UUID employeeId, LocalDate fromDate, LocalDate toDate, 
      String status, String leaveType, Pageable pageable) {
    
    Page<LeaveRequest> page = leaveRepository.findAllBy(employeeId, fromDate, toDate, status, leaveType, pageable);
    return PageResponse.from(page.map(leaveMapper::toResponse));
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'leave:approve')")
  public PageResponse<LeaveRequestResponse> getPending(Pageable pageable) {
    Page<LeaveRequest> page = leaveRepository.findByStatus("PENDING", pageable);
    return PageResponse.from(page.map(leaveMapper::toResponse));
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public PageResponse<LeaveRequestResponse> findByEmployee(UUID employeeId, Pageable pageable) {
    Page<LeaveRequest> page = leaveRepository.findByEmployeeId(employeeId, pageable);
    return PageResponse.from(page.map(leaveMapper::toResponse));
  }

  @PreAuthorize("hasPermission(null, 'leave:approve')")
  public LeaveRequestResponse approve(UUID id) {
    var leave = leaveRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LEAVE_NOT_FOUND, id));
    
    if (!"PENDING".equals(leave.getStatus())) {
      throw new BusinessException(ErrorCode.BAD_REQUEST, "Only PENDING requests can be approved");
    }

    leave.setStatus("APPROVED");
    var approved = leaveRepository.save(leave);
    log.info("Leave approved: id={} by current user", id);
    return leaveMapper.toResponse(approved);
  }

  @PreAuthorize("hasPermission(null, 'leave:approve')")
  public LeaveRequestResponse reject(UUID id, String reason) {
    var leave = leaveRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.LEAVE_NOT_FOUND, id));
    
    if (!"PENDING".equals(leave.getStatus())) {
      throw new BusinessException(ErrorCode.BAD_REQUEST, "Only PENDING requests can be rejected");
    }

    leave.setStatus("REJECTED");
    leave.setRejectReason(reason);
    var rejected = leaveRepository.save(leave);
    log.info("Leave rejected: id={} reason={}", id, reason);
    return leaveMapper.toResponse(rejected);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public LeaveStatsResponse getStatsByType() {
    Object[] result = leaveRepository.getStatsByTypeRaw(LocalDate.now().minusMonths(1));
    
    // Convert Object[] to LeaveStatsResponse
    // Handle case where result might be empty or partial
    long annual = (result != null && result.length > 0 && result[0] instanceof Number) ? ((Number) result[0]).longValue() : 0L;
    long sick = (result != null && result.length > 1 && result[1] instanceof Number) ? ((Number) result[1]).longValue() : 0L;
    long maternity = (result != null && result.length > 2 && result[2] instanceof Number) ? ((Number) result[2]).longValue() : 0L;
    long unpaid = (result != null && result.length > 3 && result[3] instanceof Number) ? ((Number) result[3]).longValue() : 0L;
    long exceptional = (result != null && result.length > 4 && result[4] instanceof Number) ? ((Number) result[4]).longValue() : 0L;
    
    return new LeaveStatsResponse(annual, sick, maternity, unpaid, exceptional);
  }

  @Transactional(readOnly = true)
  @PreAuthorize("hasPermission(null, 'leave:read')")
  public List getLeaveBalances() {
    return leaveRepository.getLeaveBalances();
  }
}
