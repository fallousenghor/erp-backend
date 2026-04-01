package com.ceremony.ceremony_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.ceremony.ceremony_backend.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contributions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contribution {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;
    
    @Column(nullable = false)
    private LocalDate contributionDate;
    
    @Column(unique = true)
    private String receiptNumber; // Format: REC-2025-00001
    
    private String transactionReference; // Pour Wave/Orange Money
    
    @ManyToOne
    @JoinColumn(name = "edition_id")
    private Edition edition;
    
    private String notes;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (contributionDate == null) {
            contributionDate = LocalDate.now();
        }
    }
}