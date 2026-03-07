package com.company.erp.modules.organization.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DepartmentUpdatedEvent extends BaseDomainEvent {

    private final UUID departmentId;
    private final String name;

    public DepartmentUpdatedEvent(UUID departmentId, String name) {
        super("DEPARTMENT_UPDATED");
        this.departmentId = departmentId;
        this.name = name;
    }
}
