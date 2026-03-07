package com.company.erp.modules.hr.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * HrEventAdapter — listens to domain events from the hr module
 * and can trigger cross-module reactions (notifications, projections, etc.)
 */
@Component
@Slf4j
public class HrEventAdapter {

    @EventListener
    public void onEvent(Object event) {
        log.debug("[HrEventAdapter] Received event: {}", event.getClass().getSimpleName());
    }
}
