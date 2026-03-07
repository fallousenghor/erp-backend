package com.company.erp.modules.finance.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * FinanceEventAdapter — listens to domain events from the finance module
 * and can trigger cross-module reactions (notifications, projections, etc.)
 */
@Component
@Slf4j
public class FinanceEventAdapter {

    @EventListener
    public void onEvent(Object event) {
        log.debug("[FinanceEventAdapter] Received event: {}", event.getClass().getSimpleName());
    }
}
