package com.ceremonie.demo.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.ceremonie.demo.enums.PaymentMethod;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiatePaymentRequest {
    
    @NotNull(message = "Le membre est obligatoire")
    private Long memberId;
    
    @NotNull(message = "Le montant est obligatoire")
    @Positive(message = "Le montant doit être positif")
    private BigDecimal amount;
    
    @NotNull(message = "La méthode de paiement est obligatoire")
    private PaymentMethod paymentMethod;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;
    
    private Long contributionId; // Optionnel : lien avec cotisation
}