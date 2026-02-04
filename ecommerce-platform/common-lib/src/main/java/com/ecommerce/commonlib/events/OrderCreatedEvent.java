package com.ecommerce.commonlib.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Événements pour communication asynchrone entre microservices
 * <p>
 * BONNE PRATIQUE: Event-Driven Architecture
 * - Découplage des services
 * - Scalabilité
 * - Traçabilité des événements
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private String orderId;
    private String userId;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private String eventId; // Pour idempotence
}
