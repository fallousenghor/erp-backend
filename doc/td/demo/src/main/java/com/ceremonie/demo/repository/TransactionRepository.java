package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Transaction;
import com.ceremonie.demo.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByCeremonialYear(CeremonialYear ceremonialYear);
    
    List<Transaction> findByType(TransactionType type);
    
    List<Transaction> findByCeremonialYearAndType(CeremonialYear ceremonialYear, TransactionType type);
    
    @Query("SELECT t FROM Transaction t WHERE t.ceremonialYear.id = :yearId AND t.deleted = false ORDER BY t.transactionDate DESC")
    List<Transaction> findByCeremonialYearId(@Param("yearId") Long yearId);
    
    @Query("SELECT t FROM Transaction t WHERE t.ceremonialYear.active = true AND t.deleted = false ORDER BY t.transactionDate DESC")
    List<Transaction> findByActiveCeremonialYear();
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.ceremonialYear.id = :yearId AND t.type = :type AND t.deleted = false")
    BigDecimal sumAmountByYearAndType(@Param("yearId") Long yearId, @Param("type") TransactionType type);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE " +
           "t.ceremonialYear.id = :yearId AND " +
           "t.type = 'ENTREE' AND t.deleted = false")
    BigDecimal getTotalIncomeByYear(@Param("yearId") Long yearId);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE " +
           "t.ceremonialYear.id = :yearId AND " +
           "t.type = 'SORTIE' AND t.deleted = false")
    BigDecimal getTotalExpenseByYear(@Param("yearId") Long yearId);
    
    @Query("SELECT t FROM Transaction t WHERE " +
           "t.transactionDate BETWEEN :startDate AND :endDate AND " +
           "t.deleted = false ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t WHERE " +
           "t.ceremonialYear.id = :yearId AND " +
           "t.type = 'SORTIE' AND " +
           "t.deleted = false GROUP BY t.category")
    List<Object[]> getExpensesByCategory(@Param("yearId") Long yearId);
    
    @Query("SELECT COUNT(t) FROM Transaction t WHERE " +
           "t.ceremonialYear.id = :yearId AND t.deleted = false")
    Long countTransactionsByYear(@Param("yearId") Long yearId);
}
