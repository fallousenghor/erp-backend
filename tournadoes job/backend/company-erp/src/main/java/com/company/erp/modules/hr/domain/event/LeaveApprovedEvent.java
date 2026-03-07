package com.company.erp.modules.hr.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class LeaveApprovedEvent extends BaseDomainEvent {

    private final UUID leaveRequestId;
    private final UUID employeeId;
    private final int daysApproved;
    private final String approvedBy;

    public LeaveApprovedEvent(UUID leaveRequestId, UUID employeeId,
                               int daysApproved, String approvedBy) {
        super("LEAVE_APPROVED");
        this.leaveRequestId = leaveRequestId;
        this.employeeId = employeeId;
        this.daysApproved = daysApproved;
        this.approvedBy = approvedBy;
    }
}
