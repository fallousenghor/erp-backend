package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ceremony.ceremony_backend.entity.Contribution;

import java.math.BigDecimal;
import java.util.List;

public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    List<Contribution> findByMemberId(Long memberId);
    List<Contribution> findByEditionId(Long editionId);
    
    @Query("SELECT SUM(c.amount) FROM Contribution c WHERE c.edition.id = ?1")
    BigDecimal getTotalByEdition(Long editionId);
    
    @Query("SELECT MAX(CAST(SUBSTRING(c.receiptNumber, 10) AS integer)) FROM Contribution c WHERE c.receiptNumber LIKE ?1")
    Integer findMaxReceiptNumberForYear(String yearPrefix);
}