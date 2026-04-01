package com.ceremonie.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import com.ceremonie.demo.enums.MaterialStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialResponse {
    private Long id;
    private String name;
    private String description;
    private String referenceNumber;
    private Integer quantity;
    private Integer availableQuantity;
    private Integer borrowedQuantity;
    private MaterialStatus status;
    private LocalDate purchaseDate;
    private String location;
    private String photoUrl;
}
