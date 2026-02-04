package com.ecommerce.commonlib.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class OrderStatusChangedEvent implements Serializable {
    private String orderId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;
    private String eventId;
}
