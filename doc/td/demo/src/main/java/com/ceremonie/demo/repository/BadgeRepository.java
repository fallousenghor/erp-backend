package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.Badge;
import com.ceremonie.demo.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    
    Optional<Badge> findByMember(Member member);
    
    Optional<Badge> findByBadgeNumber(String badgeNumber);
    
    Boolean existsByBadgeNumber(String badgeNumber);
    
    List<Badge> findByActiveTrue();
    
    @Query("SELECT b FROM Badge b WHERE b.member.id = :memberId")
    Optional<Badge> findByMemberId(Long memberId);
    
    @Query("SELECT COUNT(b) FROM Badge b WHERE b.active = true AND b.deleted = false")
    Long countActiveBadges();
    
    @Query("SELECT b FROM Badge b WHERE b.expiryDate < CURRENT_DATE AND b.active = true")
    List<Badge> findExpiredBadges();
}