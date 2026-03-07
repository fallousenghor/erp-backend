package com.company.erp.modules.education.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class ProgramCreatedEvent extends BaseDomainEvent {
    private final UUID programId;
    private final String title;

    public ProgramCreatedEvent(UUID programId, String title) {
        super("PROGRAM_CREATED");
        this.programId = programId;
        this.title = title;
    }
}
