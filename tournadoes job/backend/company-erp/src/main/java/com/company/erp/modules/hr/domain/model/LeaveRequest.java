package com.company.erp.modules.hr.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LeaveRequest {

  @Id
  @GeneratedValue
  UUID id;

  UUID employeeId;
  String employeeNumber;
  String employeeName;
  UUID departmentId;
  String departmentName;

  String leaveType;
  LocalDate startDate;
  LocalDate endDate;
  int daysRequested;
  String reason;
  String status = "PENDING";
  UUID approvedBy;
  @Column(columnDefinition = "timestamp")
  LocalDateTime approvedAt;
  String rejectReason;

}
