package com.company.erp.modules.attendance.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
  @JsonFormat(pattern = "HH:mm")
  LocalTime checkInTime;

  @Column
  @JsonFormat(pattern = "HH:mm")
  LocalTime checkOutTime;

  @Column(columnDefinition = "varchar(20)")
  String workedHours;

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

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}

