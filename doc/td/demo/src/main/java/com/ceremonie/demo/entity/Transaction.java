package com.ceremonie.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.TransactionType;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "ceremonial_year_id", nullable = false)
    private CeremonialYear ceremonialYear;
    
    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String description;
    
    @Column(name = "category")
    private String category; // Logistique, Nourriture, Transport, etc.
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    
    @Column(name = "reference_number")
    private String referenceNumber;
    
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // Si lié à un membre (cotisation, don)
    
    @ManyToOne
    @JoinColumn(name = "recorded_by")
    private User recordedBy;
    
    @Column(name = "receipt_url")
    private String receiptUrl;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}