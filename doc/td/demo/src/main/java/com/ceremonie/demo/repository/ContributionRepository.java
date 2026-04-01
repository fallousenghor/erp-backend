package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Contribution;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.enums.ContributionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContributionRepository extends JpaRepository<Contribution, Long> {
    
    List<Contribution> findByMember(Member member);
    
    List<Contribution> findByCeremonialYear(CeremonialYear ceremonialYear);
    
    List<Contribution> findByStatus(ContributionStatus status);
    
    Optional<Contribution> findByMemberAndCeremonialYear(Member member, CeremonialYear ceremonialYear);
    
    @Query("SELECT c FROM Contribution c WHERE c.ceremonialYear.id = :yearId AND c.deleted = false")
    List<Contribution> findByCeremonialYearId(@Param("yearId") Long yearId);
    
    @Query("SELECT c FROM Contribution c WHERE c.member.id = :memberId AND c.deleted = false ORDER BY c.ceremonialYear.year DESC")
    List<Contribution> findByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT c FROM Contribution c WHERE c.ceremonialYear.active = true AND c.deleted = false")
    List<Contribution> findByActiveCeremonialYear();
    
    @Query("SELECT c FROM Contribution c WHERE " +
           "c.ceremonialYear.active = true AND " +
           "c.status IN ('IMPAYE', 'PARTIEL') AND " +
           "c.deleted = false")
    List<Contribution> findUnpaidContributions();
    
    @Query("SELECT SUM(c.paidAmount) FROM Contribution c WHERE " +
           "c.ceremonialYear.id = :yearId AND c.deleted = false")
    BigDecimal getTotalPaidByYear(@Param("yearId") Long yearId);
    
    @Query("SELECT SUM(c.expectedAmount) FROM Contribution c WHERE " +
           "c.ceremonialYear.id = :yearId AND c.deleted = false")
    BigDecimal getTotalExpectedByYear(@Param("yearId") Long yearId);
    
    @Query("SELECT COUNT(c) FROM Contribution c WHERE " +
           "c.ceremonialYear.id = :yearId AND " +
           "c.status = 'PAYE' AND c.deleted = false")
    Long countPaidContributionsByYear(@Param("yearId") Long yearId);
    
    @Query("SELECT COUNT(c) FROM Contribution c WHERE " +
           "c.ceremonialYear.id = :yearId AND " +
           "c.status = 'IMPAYE' AND c.deleted = false")
    Long countUnpaidContributionsByYear(@Param("yearId") Long yearId);
}