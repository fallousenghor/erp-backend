package com.company.erp.modules.inventory.infrastructure.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * InventoryEventAdapter — listens to domain events from the inventory module
 * and can trigger cross-module reactions (notifications, projections, etc.)
 */
@Component
@Slf4j
public class InventoryEventAdapter {

    @EventListener
    public void onEvent(Object event) {
        log.debug("[InventoryEventAdapter] Received event: {}", event.getClass().getSimpleName());
    }
}
