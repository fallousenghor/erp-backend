package com.ceremonie.demo.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ceremonie.demo.dto.request.InitiatePaymentRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.PaymentResponse;
import com.ceremonie.demo.services.interfaces.PaymentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payments", description = "Gestion des paiements mobiles")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @Operation(summary = "Initier un paiement", description = "Initie un paiement via Wave ou Orange Money")
    public ResponseEntity<ApiResponse<PaymentResponse>> initiatePayment(
            @Valid @RequestBody InitiatePaymentRequest request) {
        PaymentResponse payment = paymentService.initiatePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Paiement initié avec succès", payment));
    }

    @GetMapping("/verify/{transactionReference}")
    @Operation(summary = "Vérifier un paiement")
    public ResponseEntity<ApiResponse<PaymentResponse>> verifyPayment(
            @PathVariable String transactionReference) {
        PaymentResponse payment = paymentService.verifyPayment(transactionReference);
        return ResponseEntity.ok(ApiResponse.success("Statut du paiement", payment));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un paiement par ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Paiement trouvé", payment));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Obtenir les paiements d'un membre")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPaymentsByMember(@PathVariable Long memberId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("Paiements du membre", payments));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Obtenir les paiements en attente")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getPendingPayments() {
        List<PaymentResponse> payments = paymentService.getPendingPayments();
        return ResponseEntity.ok(ApiResponse.success("Paiements en attente", payments));
    }

    // ========================================
    // WEBHOOKS / CALLBACKS
    // ========================================
    
    @PostMapping("/callback/wave")
    @Operation(summary = "Callback Wave", description = "Endpoint pour les notifications Wave")
    public ResponseEntity<String> handleWaveCallback(@RequestBody Map<String, Object> payload) {
        try {
            log.info("Callback Wave reçu: {}", payload);
            
            String paymentId = (String) payload.get("id");
            String status = (String) payload.get("status");
            
            paymentService.handleWaveCallback(paymentId, status);
            
            return ResponseEntity.ok("Callback traité");
        } catch (Exception e) {
            log.error("Erreur lors du traitement du callback Wave: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur");
        }
    }

    @PostMapping("/callback/orange-money")
    @Operation(summary = "Callback Orange Money", description = "Endpoint pour les notifications Orange Money")
    public ResponseEntity<String> handleOrangeMoneyCallback(@RequestBody Map<String, Object> payload) {
        try {
            log.info("Callback Orange Money reçu: {}", payload);
            
            String token = (String) payload.get("payment_token");
            String status = (String) payload.get("status");
            
            paymentService.handleOrangeMoneyCallback(token, status);
            
            return ResponseEntity.ok("Callback traité");
        } catch (Exception e) {
            log.error("Erreur lors du traitement du callback Orange Money: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur");
        }
    }

    @PostMapping("/callback/orange-money/notify")
    @Operation(summary = "Notification Orange Money")
    public ResponseEntity<String> handleOrangeMoneyNotification(@RequestParam Map<String, String> params) {
        try {
            log.info("Notification Orange Money reçue: {}", params);
            
            String token = params.get("payment_token");
            String status = params.get("status");
            
            paymentService.handleOrangeMoneyCallback(token, status);
            
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Erreur lors du traitement de la notification Orange Money: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR");
        }
    }
}
