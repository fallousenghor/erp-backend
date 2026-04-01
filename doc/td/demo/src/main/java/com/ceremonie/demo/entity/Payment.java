package com.ceremonie.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.PaymentStatus;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(name = "transaction_reference", unique = true, nullable = false)
    private String transactionReference; // Référence Wave ou Orange Money
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.EN_ATTENTE;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "initiated_at", nullable = false)
    private LocalDateTime initiatedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "wave_payment_id")
    private String wavePaymentId; // ID retourné par Wave
    
    @Column(name = "orange_money_token")
    private String orangeMoneyToken; // Token Orange Money
    
    @Column(columnDefinition = "TEXT")
    private String errorMessage;
    
    @OneToOne
    @JoinColumn(name = "contribution_id")
    private Contribution contribution;
    
    @Column(name = "receipt_url")
    private String receiptUrl;
}