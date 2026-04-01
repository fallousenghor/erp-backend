package com.ceremony.ceremony_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import com.ceremony.ceremony_backend.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private LocalDate transactionDate;
    
    @ManyToOne
    @JoinColumn(name = "edition_id")
    private Edition edition;
    
    @ManyToOne
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;
    
    private String category; // Ex: Transport, Alimentation, Matériel
    
    @Column(unique = true)
    private String referenceNumber;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (transactionDate == null) {
            transactionDate = LocalDate.now();
        }
    }
}