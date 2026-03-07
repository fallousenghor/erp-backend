package com.company.erp.modules.hr.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class EmployeeTerminatedEvent extends BaseDomainEvent {

    private final UUID employeeId;
    private final String employeeNumber;
    private final LocalDate terminationDate;

    public EmployeeTerminatedEvent(UUID employeeId, String employeeNumber, LocalDate terminationDate) {
        super("EMPLOYEE_TERMINATED");
        this.employeeId = employeeId;
        this.employeeNumber = employeeNumber;
        this.terminationDate = terminationDate;
    }
}
