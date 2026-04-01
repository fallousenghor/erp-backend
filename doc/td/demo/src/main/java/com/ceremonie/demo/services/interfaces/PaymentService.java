package com.ceremonie.demo.services.interfaces;


import java.util.List;

import com.ceremonie.demo.dto.request.InitiatePaymentRequest;
import com.ceremonie.demo.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse initiatePayment(InitiatePaymentRequest request);
    PaymentResponse verifyPayment(String transactionReference);
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getPaymentsByMember(Long memberId);
    List<PaymentResponse> getPendingPayments();
    void handleWaveCallback(String paymentId, String status);
    void handleOrangeMoneyCallback(String token, String status);
}
