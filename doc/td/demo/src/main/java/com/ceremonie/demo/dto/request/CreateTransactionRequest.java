package com.ceremonie.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.TransactionType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {
    
    @NotNull(message = "L'année cérémoniale est obligatoire")
    private Long ceremonialYearId;
    
    @NotNull(message = "La date de transaction est obligatoire")
    private LocalDate transactionDate;
    
    @NotNull(message = "Le type de transaction est obligatoire")
    private TransactionType type;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;
    
    @NotBlank(message = "La description est obligatoire")
    private String description;
    
    private String category;
    
    private PaymentMethod paymentMethod;
    
    private String referenceNumber;
    
    private Long memberId;
    
    private String notes;
}
