package com.ceremonie.demo.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.ceremonie.demo.dto.request.InitiatePaymentRequest;
import com.ceremonie.demo.dto.response.PaymentResponse;
import com.ceremonie.demo.entity.Contribution;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.entity.Payment;
import com.ceremonie.demo.enums.ContributionStatus;
import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.PaymentStatus;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.ContributionRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.repository.PaymentRepository;
import com.ceremonie.demo.services.interfaces.PaymentService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final ContributionRepository contributionRepository;
    private final RestTemplate restTemplate;

    @Value("${wave.api.url:https://api.wave.com/v1}")
    private String waveApiUrl;

    @Value("${wave.api.key:your-wave-api-key}")
    private String waveApiKey;

    @Value("${orange.money.api.url:https://api.orange.com/orange-money-webpay/dev/v1}")
    private String orangeMoneyApiUrl;

    @Value("${orange.money.merchant.key:your-orange-merchant-key}")
    private String orangeMerchantKey;

    @Value("${orange.money.merchant.id:your-merchant-id}")
    private String orangeMerchantId;

    @Value("${app.callback.url:http://localhost:8080/api/payments/callback}")
    private String callbackUrl;

    @Override
    @Transactional
    public PaymentResponse initiatePayment(InitiatePaymentRequest request) {
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));

        String transactionReference = generateTransactionReference();

        Payment payment = Payment.builder()
                .member(member)
                .transactionReference(transactionReference)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.EN_ATTENTE)
                .phoneNumber(request.getPhoneNumber())
                .initiatedAt(LocalDateTime.now())
                .build();

        if (request.getContributionId() != null) {
            Contribution contribution = contributionRepository.findById(request.getContributionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cotisation non trouvée"));
            payment.setContribution(contribution);
        }

        Payment savedPayment = paymentRepository.save(payment);

        // Initier le paiement selon la méthode
        try {
            if (request.getPaymentMethod() == PaymentMethod.WAVE) {
                initiateWavePayment(savedPayment);
            } else if (request.getPaymentMethod() == PaymentMethod.ORANGE_MONEY) {
                initiateOrangeMoneyPayment(savedPayment);
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'initiation du paiement: {}", e.getMessage());
            savedPayment.setStatus(PaymentStatus.ECHOUE);
            savedPayment.setErrorMessage(e.getMessage());
            paymentRepository.save(savedPayment);
        }

        return mapToResponse(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponse verifyPayment(String transactionReference) {
        Payment payment = paymentRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));

        // Vérifier le statut selon la méthode de paiement
        if (payment.getPaymentMethod() == PaymentMethod.WAVE) {
            verifyWavePayment(payment);
        } else if (payment.getPaymentMethod() == PaymentMethod.ORANGE_MONEY) {
            verifyOrangeMoneyPayment(payment);
        }

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));
        return mapToResponse(payment);
    }

    @Override
    public List<PaymentResponse> getPaymentsByMember(Long memberId) {
        return paymentRepository.findByMemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponse> getPendingPayments() {
        return paymentRepository.findPendingPayments().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void handleWaveCallback(String paymentId, String status) {
        Payment payment = paymentRepository.findByTransactionReference(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement non trouvé"));

        if ("SUCCESS".equalsIgnoreCase(status) || "COMPLETED".equalsIgnoreCase(status)) {
            payment.setStatus(PaymentStatus.REUSSI);
            payment.setCompletedAt(LocalDateTime.now());
            
            // Mettre à jour la cotisation si liée
            if (payment.getContribution() != null) {
                updateContribution(payment);
            }
        } else {
            payment.setStatus(PaymentStatus.ECHOUE);
            payment.setErrorMessage("Paiement échoué: " + status);
        }

        paymentRepository.save(payment);
        log.info("Callback Wave traité pour le paiement: {}", paymentId);
    }

    @Override
    @Transactional
    public void handleOrangeMoneyCallback(String token, String status) {
        // Le token peut être utilisé comme transactionReference
        Payment payment = paymentRepository.findByTransactionReference(token)
                .orElse(null);

        if (payment == null) {
            log.warn("Paiement non trouvé pour le token: {}", token);
            return;
        }

        if ("SUCCESS".equalsIgnoreCase(status)) {
            payment.setStatus(PaymentStatus.REUSSI);
            payment.setCompletedAt(LocalDateTime.now());
            
            if (payment.getContribution() != null) {
                updateContribution(payment);
            }
        } else {
            payment.setStatus(PaymentStatus.ECHOUE);
            payment.setErrorMessage("Paiement échoué: " + status);
        }

        paymentRepository.save(payment);
        log.info("Callback Orange Money traité pour le token: {}", token);
    }

    // ========================================
    // WAVE API INTEGRATION
    // ========================================
    private void initiateWavePayment(Payment payment) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + waveApiKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("amount", payment.getAmount());
            requestBody.put("currency", "XOF"); // Franc CFA
            requestBody.put("phone_number", payment.getPhoneNumber());
            requestBody.put("transaction_reference", payment.getTransactionReference());
            requestBody.put("callback_url", callbackUrl + "/wave");
            requestBody.put("description", "Cotisation membre");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    waveApiUrl + "/checkout/sessions",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String wavePaymentId = (String) responseBody.get("id");
                    payment.setWavePaymentId(wavePaymentId);
                    paymentRepository.save(payment);
                    
                    log.info("Paiement Wave initié avec succès: {}", wavePaymentId);
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'initiation du paiement Wave: {}", e.getMessage());
            throw new RuntimeException("Erreur Wave API: " + e.getMessage());
        }
    }

    private void verifyWavePayment(Payment payment) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + waveApiKey);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    waveApiUrl + "/checkout/sessions/" + payment.getWavePaymentId(),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String status = (String) responseBody.get("status");
                    
                    if ("COMPLETED".equalsIgnoreCase(status)) {
                        payment.setStatus(PaymentStatus.REUSSI);
                        payment.setCompletedAt(LocalDateTime.now());
                        
                        if (payment.getContribution() != null) {
                            updateContribution(payment);
                        }
                    } else if ("FAILED".equalsIgnoreCase(status)) {
                        payment.setStatus(PaymentStatus.ECHOUE);
                    }
                    
                    paymentRepository.save(payment);
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du paiement Wave: {}", e.getMessage());
        }
    }

    // ========================================
    // ORANGE MONEY API INTEGRATION
    // ========================================
    private void initiateOrangeMoneyPayment(Payment payment) {
        try {
            // Étape 1: Obtenir le token d'accès
            String accessToken = getOrangeMoneyAccessToken();

            // Étape 2: Initier le paiement
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("merchant_key", orangeMerchantKey);
            requestBody.put("currency", "OUV"); // Orange Unit Value
            requestBody.put("order_id", payment.getTransactionReference());
            requestBody.put("amount", payment.getAmount().intValue());
            requestBody.put("return_url", callbackUrl + "/orange-money");
            requestBody.put("cancel_url", callbackUrl + "/orange-money/cancel");
            requestBody.put("notif_url", callbackUrl + "/orange-money/notify");
            requestBody.put("lang", "fr");
            requestBody.put("reference", "Cotisation membre");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    orangeMoneyApiUrl + "/webpayment",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String paymentToken = (String) responseBody.get("payment_token");
                    String paymentUrl = (String) responseBody.get("payment_url");
                    
                    payment.setOrangeMoneyToken(paymentToken);
                    paymentRepository.save(payment);
                    
                    log.info("Paiement Orange Money initié: {}", paymentToken);
                    log.info("URL de paiement: {}", paymentUrl);
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'initiation du paiement Orange Money: {}", e.getMessage());
            throw new RuntimeException("Erreur Orange Money API: " + e.getMessage());
        }
    }

    private String getOrangeMoneyAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth(orangeMerchantId, orangeMerchantKey);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("grant_type", "client_credentials");

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    orangeMoneyApiUrl + "/oauth/token",
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    return (String) responseBody.get("access_token");
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de l'obtention du token Orange Money: {}", e.getMessage());
        }
        
        throw new RuntimeException("Impossible d'obtenir le token Orange Money");
    }

    private void verifyOrangeMoneyPayment(Payment payment) {
        try {
            String accessToken = getOrangeMoneyAccessToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    orangeMoneyApiUrl + "/webpayment/" + payment.getOrangeMoneyToken(),
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null) {
                    String status = (String) responseBody.get("status");
                    
                    if ("SUCCESS".equalsIgnoreCase(status)) {
                        payment.setStatus(PaymentStatus.REUSSI);
                        payment.setCompletedAt(LocalDateTime.now());
                        
                        if (payment.getContribution() != null) {
                            updateContribution(payment);
                        }
                    } else if ("FAILED".equalsIgnoreCase(status) || "EXPIRED".equalsIgnoreCase(status)) {
                        payment.setStatus(PaymentStatus.ECHOUE);
                        payment.setErrorMessage("Paiement échoué: " + status);
                    }
                    
                    paymentRepository.save(payment);
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du paiement Orange Money: {}", e.getMessage());
        }
    }

    // ========================================
    // HELPER METHODS
    // ========================================
    private String generateTransactionReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void updateContribution(Payment payment) {
        Contribution contribution = payment.getContribution();
        contribution.setPaidAmount(contribution.getPaidAmount().add(payment.getAmount()));
        contribution.setPaymentDate(payment.getCompletedAt().toLocalDate());
        
        // Mettre à jour le statut
        if (contribution.getPaidAmount().compareTo(contribution.getExpectedAmount()) >= 0) {
            contribution.setStatus(ContributionStatus.PAYE);
        } else {
            contribution.setStatus(ContributionStatus.PARTIEL);
        }
        
        contributionRepository.save(contribution);
        log.info("Cotisation mise à jour pour le membre: {}", payment.getMember().getMemberNumber());
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .memberId(payment.getMember().getId())
                .memberName(payment.getMember().getFirstName() + " " + payment.getMember().getLastName())
                .transactionReference(payment.getTransactionReference())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .status(payment.getStatus())
                .phoneNumber(payment.getPhoneNumber())
                .initiatedAt(payment.getInitiatedAt())
                .completedAt(payment.getCompletedAt())
                .receiptUrl(payment.getReceiptUrl())
                .errorMessage(payment.getErrorMessage())
                .build();
    }
}