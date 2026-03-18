package com.company.erp.modules.accounting.application.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class JournalEntryResponse {
    
    UUID id;
    String reference;
    LocalDateTime entryDate;
    String description;
    BigDecimal debitAmount;
    BigDecimal creditAmount;
    String accountCode;
    String accountName;
    String currency;
    String type;
    String notes;
    boolean balanced;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

