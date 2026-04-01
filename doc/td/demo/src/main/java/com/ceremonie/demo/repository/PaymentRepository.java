package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.entity.Payment;
import com.ceremonie.demo.enums.PaymentMethod;
import com.ceremonie.demo.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionReference(String transactionReference);

    List<Payment> findByMember(Member member);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);
    
    @Query("SELECT p FROM Payment p WHERE p.member.id = :memberId AND p.deleted = false ORDER BY p.initiatedAt DESC")
    List<Payment> findByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'EN_ATTENTE' AND p.deleted = false")
    List<Payment> findPendingPayments();
    
    @Query("SELECT p FROM Payment p WHERE " +
           "p.status = 'EN_ATTENTE' AND " +
           "p.initiatedAt < :timeout AND " +
           "p.deleted = false")
    List<Payment> findTimedOutPayments(@Param("timeout") LocalDateTime timeout);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE " +
           "p.status = 'REUSSI' AND " +
           "p.paymentMethod = :method AND " +
           "p.deleted = false")
    BigDecimal getTotalByPaymentMethod(@Param("method") PaymentMethod method);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE " +
           "p.status = 'REUSSI' AND p.deleted = false")
    Long countSuccessfulPayments();
    
    @Query("SELECT p FROM Payment p WHERE " +
           "p.initiatedAt BETWEEN :startDate AND :endDate AND " +
           "p.deleted = false ORDER BY p.initiatedAt DESC")
    List<Payment> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
