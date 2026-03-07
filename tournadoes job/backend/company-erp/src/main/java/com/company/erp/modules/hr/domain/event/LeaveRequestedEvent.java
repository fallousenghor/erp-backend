package com.company.erp.modules.hr.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class LeaveRequestedEvent extends BaseDomainEvent {

    private final UUID leaveRequestId;
    private final UUID employeeId;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public LeaveRequestedEvent(UUID leaveRequestId, UUID employeeId,
                                LocalDate startDate, LocalDate endDate) {
        super("LEAVE_REQUESTED");
        this.leaveRequestId = leaveRequestId;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
