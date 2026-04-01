package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.CeremonialYear;

import java.util.List;
import java.util.Optional;

@Repository
public interface CeremonialYearRepository extends JpaRepository<CeremonialYear, Long> {
    
    Optional<CeremonialYear> findByYear(Integer year);
    
    Optional<CeremonialYear> findByActiveTrue();
    
    Boolean existsByYear(Integer year);
    
    List<CeremonialYear> findAllByOrderByYearDesc();
    
    @Query("SELECT cy FROM CeremonialYear cy WHERE cy.deleted = false ORDER BY cy.year DESC")
    List<CeremonialYear> findAllActive();
    
    @Query("SELECT cy FROM CeremonialYear cy WHERE cy.active = true AND cy.deleted = false")
    Optional<CeremonialYear> findActiveCeremonialYear();
    
    @Query("SELECT COUNT(cy) FROM CeremonialYear cy WHERE cy.deleted = false")
    Long countActiveCeremonialYears();
    
    @Query("SELECT SUM(cy.totalIncome) FROM CeremonialYear cy WHERE cy.deleted = false")
    java.math.BigDecimal getTotalIncomeAllYears();
    
    @Query("SELECT SUM(cy.totalExpense) FROM CeremonialYear cy WHERE cy.deleted = false")
    java.math.BigDecimal getTotalExpenseAllYears();
}