package com.company.erp.shared.event;

import com.company.erp.shared.base.BaseDomainEvent;

/**
 * Marker interface for domain event handlers.
 * Implement this interface in any adapter that must react to a domain event.
 *
 * @param <E> the specific domain event type to handle
 */
public interface DomainEventHandler<E extends BaseDomainEvent> {

    void handle(E event);
}
