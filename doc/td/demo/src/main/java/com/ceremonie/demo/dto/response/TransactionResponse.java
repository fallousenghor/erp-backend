package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.TransactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private Long ceremonialYearId;
    private Integer ceremonialYear;
    private LocalDate transactionDate;
    private TransactionType type;
    private BigDecimal amount;
    private String description;
    private String category;
    private PaymentMethod paymentMethod;
    private String referenceNumber;
    private String memberName;
    private String recordedByName;
    private String receiptUrl;
}
