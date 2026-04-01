package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.PaymentStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long memberId;
    private String memberName;
    private String transactionReference;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String phoneNumber;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private String receiptUrl;
    private String errorMessage;
}