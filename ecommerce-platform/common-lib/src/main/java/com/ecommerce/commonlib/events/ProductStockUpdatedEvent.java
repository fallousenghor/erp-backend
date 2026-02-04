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
class ProductStockUpdatedEvent implements Serializable {
    private String productId;
    private Integer oldStock;
    private Integer newStock;
    private LocalDateTime updatedAt;
    private String eventId;
}
