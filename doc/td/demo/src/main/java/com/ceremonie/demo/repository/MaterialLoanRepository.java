package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.Material;
import com.ceremonie.demo.entity.MaterialLoan;
import com.ceremonie.demo.entity.Member;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaterialLoanRepository extends JpaRepository<MaterialLoan, Long> {
    
    List<MaterialLoan> findByMaterial(Material material);
    
    List<MaterialLoan> findByMember(Member member);
    
    List<MaterialLoan> findByReturnedFalse();
    
    @Query("SELECT ml FROM MaterialLoan ml WHERE ml.material.id = :materialId AND ml.deleted = false")
    List<MaterialLoan> findByMaterialId(@Param("materialId") Long materialId);
    
    @Query("SELECT ml FROM MaterialLoan ml WHERE ml.member.id = :memberId AND ml.deleted = false ORDER BY ml.borrowDate DESC")
    List<MaterialLoan> findByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT ml FROM MaterialLoan ml WHERE " +
           "ml.returned = false AND ml.deleted = false ORDER BY ml.borrowDate DESC")
    List<MaterialLoan> findActiveLoans();
    
    @Query("SELECT ml FROM MaterialLoan ml WHERE " +
           "ml.returned = false AND " +
           "ml.expectedReturnDate < CURRENT_DATE AND " +
           "ml.deleted = false")
    List<MaterialLoan> findOverdueLoans();
    
    @Query("SELECT COUNT(ml) FROM MaterialLoan ml WHERE " +
           "ml.material.id = :materialId AND " +
           "ml.returned = false AND " +
           "ml.deleted = false")
    Long countActiveLoansByMaterial(@Param("materialId") Long materialId);
    
    @Query("SELECT ml FROM MaterialLoan ml WHERE " +
           "ml.borrowDate BETWEEN :startDate AND :endDate AND " +
           "ml.deleted = false ORDER BY ml.borrowDate DESC")
    List<MaterialLoan> findByDateRange(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
