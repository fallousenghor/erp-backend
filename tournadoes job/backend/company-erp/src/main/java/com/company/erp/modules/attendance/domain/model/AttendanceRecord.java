package com.company.erp.modules.attendance.domain.model;

// import com.company.erp.shared.domain.AggregateRoot; // Use standard JPA Entity
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "attendance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "id")
  UUID id;

  @Column(nullable = false)
  UUID employeeId;

  @Column(nullable = false, length = 50)
  String employeeNumber;

  @Column(nullable = false, length = 160)
  String employeeName;

  @Column
  UUID departmentId;

  @Column(length = 100)
  String departmentName;

  @Column(nullable = false)
  LocalDate recordDate;

  @Column
  LocalTime checkInTime;

  @Column
  LocalTime checkOutTime;

  @Column(columnDefinition = "interval")
  Duration workedHours;

  @Column(nullable = false, length = 20)
  String status;

  @Column
  Integer lateMinutes;

  @Column(columnDefinition = "TEXT")
  String notes;

  @Column(length = 200)
  String location;

  @Column(length = 45)
  String ipAddress;

  @Column(length = 255)
  String deviceInfo;
}

