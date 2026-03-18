package com.company.erp.modules.accounting.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CreateJournalEntryRequest {
    
    @NotBlank
    @Size(max = 100)
    String reference;
    
    @NotNull
    LocalDateTime entryDate;
    
    String description;
    
    @NotNull
    @DecimalMin("0.0")
    BigDecimal debitAmount;
    
    @NotNull
    @DecimalMin("0.0")
    BigDecimal creditAmount;
    
    @NotBlank
    @Size(max = 20)
    String accountCode;
    
    @NotBlank
    @Size(max = 100)
    String accountName;
    
    String currency;
    
    String notes;
}

