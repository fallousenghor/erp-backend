package com.company.erp.modules.auth.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserLockedEvent extends BaseDomainEvent {

    private final UUID userId;
    private final String username;
    private final int failedAttempts;

    public UserLockedEvent(UUID userId, String username, int failedAttempts) {
        super("USER_LOCKED");
        this.userId = userId;
        this.username = username;
        this.failedAttempts = failedAttempts;
    }
}
