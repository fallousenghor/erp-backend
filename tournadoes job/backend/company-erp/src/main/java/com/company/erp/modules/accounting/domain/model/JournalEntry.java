package com.company.erp.modules.accounting.domain.model;

import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "journal_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JournalEntry {
    
    @Id
    private UUID id;

    @Column(nullable = false)
    String reference;

    @Column(nullable = false)
    LocalDateTime entryDate;

    String description;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal debitAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal creditAmount;

    @Column(nullable = false)
    String accountCode;

    @Column(nullable = false)
    String accountName;

    String currency;

    @Enumerated(EnumType.STRING)
    JournalEntryType type;

    @Column(length = 1000)
    String notes;

    public enum JournalEntryType {
        DEBIT, CREDIT, ADJUSTMENT
    }

    public boolean isBalanced() {
        return debitAmount.compareTo(creditAmount) == 0;
    }
}

