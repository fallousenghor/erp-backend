package com.ceremonie.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ceremonie.demo.dto.request.CreateTransactionRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.FinancialReportResponse;
import com.ceremonie.demo.dto.response.TransactionResponse;
import com.ceremonie.demo.services.interfaces.TransactionService;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Gestion de la comptabilité")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Créer une transaction")
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
            @Valid @RequestBody CreateTransactionRequest request) {
        TransactionResponse transaction = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Transaction enregistrée", transaction));
    }

    @GetMapping("/active-year")
    @Operation(summary = "Transactions de l'année active")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionsByActiveYear() {
        List<TransactionResponse> transactions = transactionService.getTransactionsByActiveYear();
        return ResponseEntity.ok(ApiResponse.success("Transactions", transactions));
    }

    @GetMapping("/report/{yearId}")
    @Operation(summary = "Rapport financier complet")
    public ResponseEntity<ApiResponse<FinancialReportResponse>> getFinancialReport(@PathVariable Long yearId) {
        FinancialReportResponse report = transactionService.getFinancialReport(yearId);
        return ResponseEntity.ok(ApiResponse.success("Rapport financier", report));
    }
}