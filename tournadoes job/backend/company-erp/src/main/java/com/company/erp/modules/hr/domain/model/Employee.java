package com.company.erp.modules.hr.domain.model;

import com.company.erp.modules.hr.domain.model.valueobject.Contract;
import com.company.erp.modules.hr.domain.model.valueobject.EmployeeStatus;
import com.company.erp.modules.hr.domain.model.valueobject.Salary;
import com.company.erp.shared.base.BaseAuditEntity;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Employee Aggregate Root.
 */
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee extends BaseAuditEntity {

    @Column(name = "employee_number", nullable = false, unique = true, length = 20)
    private String employeeNumber;

    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Embedded
    private Salary salary;

    @Embedded
    private Contract contract;

    // Cross-module reference by ID (no JPA join across modules)
    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "department_name", length = 100)
    private String departmentName;

    @Column(name = "position_id")
    private UUID positionId;

    @Column(name = "position_title", length = 100)
    private String positionTitle;

    @Column(name = "leave_balance", nullable = false)
    @Builder.Default
    private int leaveBalance = 20;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "qr_code_url", length = 500)
    private String qrCodeUrl;

    // ── Domain behavior ─────────────────────────────────────────────────────

    public void terminate(LocalDate terminationDate) {
        if (this.status == EmployeeStatus.TERMINATED) {
            throw new BusinessException(ErrorCode.EMPLOYEE_ALREADY_TERMINATED);
        }
        this.status = EmployeeStatus.TERMINATED;
        this.terminationDate = terminationDate;
    }

    public void setOnLeave() {
        this.status = EmployeeStatus.ON_LEAVE;
    }

    public void returnFromLeave() {
        this.status = EmployeeStatus.ACTIVE;
    }

    public void deductLeaveBalance(int days) {
        if (days > this.leaveBalance) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_LEAVE_BALANCE,
                    "Requested: " + days + ", Available: " + this.leaveBalance);
        }
        this.leaveBalance -= days;
    }

    public void restoreLeaveBalance(int days) {
        this.leaveBalance += days;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return this.status == EmployeeStatus.ACTIVE;
    }
}
