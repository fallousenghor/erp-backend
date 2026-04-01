package com.ceremonie.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ceremonie.demo.enums.ContributionStatus;

@Entity
@Table(name = "contributions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contribution extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ceremonial_year_id", nullable = false)
    @JsonIgnore
    private CeremonialYear ceremonialYear;
    
    @Column(name = "expected_amount", nullable = false)
    private BigDecimal expectedAmount;
    
    @Column(name = "paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContributionStatus status = ContributionStatus.IMPAYE;
    
    @Column(name = "due_date")
    private LocalDate dueDate;
    
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    
    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction; // Lien avec transaction
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}
