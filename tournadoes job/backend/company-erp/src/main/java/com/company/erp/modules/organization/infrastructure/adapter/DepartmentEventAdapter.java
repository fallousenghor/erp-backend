package com.company.erp.modules.organization.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * DepartmentEventAdapter — listens to domain events from the department module
 * and can trigger cross-module reactions (notifications, projections, etc.)
 */
@Component
@Slf4j
public class DepartmentEventAdapter {

    @EventListener
    public void onEvent(Object event) {
        log.debug("[DepartmentEventAdapter] Received event: {}", event.getClass().getSimpleName());
    }
}
