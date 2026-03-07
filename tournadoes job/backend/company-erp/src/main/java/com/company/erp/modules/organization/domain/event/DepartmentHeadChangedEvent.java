package com.company.erp.modules.organization.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class DepartmentHeadChangedEvent extends BaseDomainEvent {

    private final UUID departmentId;
    private final UUID newHeadEmployeeId;
    private final String newHeadName;

    public DepartmentHeadChangedEvent(UUID departmentId, UUID newHeadEmployeeId, String newHeadName) {
        super("DEPARTMENT_HEAD_CHANGED");
        this.departmentId = departmentId;
        this.newHeadEmployeeId = newHeadEmployeeId;
        this.newHeadName = newHeadName;
    }
}
