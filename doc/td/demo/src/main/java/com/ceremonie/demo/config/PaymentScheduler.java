package com.ceremonie.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ceremonie.demo.entity.Payment;
import com.ceremonie.demo.enums.PaymentStatus;
import com.ceremonie.demo.repository.PaymentRepository;
import com.ceremonie.demo.services.interfaces.PaymentService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {

    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;

    // Vérifier les paiements en attente toutes les 5 minutes
    @Scheduled(fixedDelay = 300000) // 5 minutes
    public void checkPendingPayments() {
        log.info("Vérification des paiements en attente...");
        
        List<Payment> pendingPayments = paymentRepository.findPendingPayments();
        
        for (Payment payment : pendingPayments) {
            try {
                paymentService.verifyPayment(payment.getTransactionReference());
            } catch (Exception e) {
                log.error("Erreur lors de la vérification du paiement {}: {}", 
                        payment.getTransactionReference(), e.getMessage());
            }
        }
        
        log.info("Vérification terminée. {} paiements vérifiés", pendingPayments.size());
    }

    // Marquer les paiements expirés (plus de 24h) comme échoués
    @Scheduled(cron = "0 0 2 * * *") // Tous les jours à 2h du matin
    public void expireOldPayments() {
        log.info("Expiration des anciens paiements...");
        
        LocalDateTime timeout = LocalDateTime.now().minusHours(24);
        List<Payment> timedOutPayments = paymentRepository.findTimedOutPayments(timeout);
        
        for (Payment payment : timedOutPayments) {
            payment.setStatus(PaymentStatus.ECHOUE);
            payment.setErrorMessage("Paiement expiré (timeout)");
            paymentRepository.save(payment);
        }
        
        log.info("{} paiements expirés", timedOutPayments.size());
    }
}
