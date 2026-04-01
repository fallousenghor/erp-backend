// repository/TransactionRepository.java
package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ceremony.ceremony_backend.entity.Transaction;
import com.ceremony.ceremony_backend.enums.TransactionType;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByEditionId(Long editionId);
    List<Transaction> findByType(TransactionType type);
    
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.edition.id = ?1 AND t.type = ?2")
    BigDecimal getTotalByEditionAndType(Long editionId, TransactionType type);
}