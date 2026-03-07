package com.company.erp.modules.auth.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;

import java.util.UUID;

@Getter
public class UserLoggedInEvent extends BaseDomainEvent {

    private final UUID userId;
    private final String username;
    private final String ipAddress;

    public UserLoggedInEvent(UUID userId, String username, String ipAddress) {
        super("USER_LOGGED_IN");
        this.userId = userId;
        this.username = username;
        this.ipAddress = ipAddress;
    }
}
