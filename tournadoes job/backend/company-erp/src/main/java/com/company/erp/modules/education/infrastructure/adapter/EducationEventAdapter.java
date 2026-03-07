package com.company.erp.modules.education.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * EducationEventAdapter — listens to domain events from the education module
 * and can trigger cross-module reactions (notifications, projections, etc.)
 */
@Component
@Slf4j
public class EducationEventAdapter {

    @EventListener
    public void onEvent(Object event) {
        log.debug("[EducationEventAdapter] Received event: {}", event.getClass().getSimpleName());
    }
}
