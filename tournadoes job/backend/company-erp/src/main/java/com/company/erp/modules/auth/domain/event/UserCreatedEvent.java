package com.company.erp.modules.auth.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserCreatedEvent extends BaseDomainEvent {

    private final UUID userId;
    private final String username;
    private final String email;

    public UserCreatedEvent(UUID userId, String username, String email) {
        super("USER_CREATED");
        this.userId = userId;
        this.username = username;
        this.email = email;
    }
}
