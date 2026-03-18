package com.company.erp.modules.finance.presentation.controller;

import com.company.erp.modules.finance.application.dto.response.PaymentResponse;
import com.company.erp.modules.finance.application.service.PaymentService;
import com.company.erp.shared.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "Finance — Payment records")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.findById(id)));
    }

    @GetMapping("/invoice/{invoiceId}")
    @Operation(summary = "Get all payments for an invoice")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> findByInvoice(
            @PathVariable UUID invoiceId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.findByInvoice(invoiceId)));
    }
}
