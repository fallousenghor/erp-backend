package com.company.erp.shared.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adapter over Spring's ApplicationEventPublisher.
 * All domain events must be published through this component
 * so they are dispatched to registered listeners.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(BaseDomainEvent event) {
        log.debug("Publishing domain event [{}] with id [{}]",
                event.getEventType(), event.getEventId());
        applicationEventPublisher.publishEvent(event);
    }
}
