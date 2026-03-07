package com.company.erp.modules.hr.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class EmployeeCreatedEvent extends BaseDomainEvent {

    private final UUID employeeId;
    private final String employeeNumber;
    private final String fullName;

    public EmployeeCreatedEvent(UUID employeeId, String employeeNumber, String fullName) {
        super("EMPLOYEE_CREATED");
        this.employeeId = employeeId;
        this.employeeNumber = employeeNumber;
        this.fullName = fullName;
    }
}
