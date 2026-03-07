package com.company.erp.modules.organization.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DepartmentCreatedEvent extends BaseDomainEvent {

    private final UUID departmentId;
    private final String name;
    private final String code;

    public DepartmentCreatedEvent(UUID departmentId, String name, String code) {
        super("DEPARTMENT_CREATED");
        this.departmentId = departmentId;
        this.name = name;
        this.code = code;
    }
}
