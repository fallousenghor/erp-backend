// repository/MemberRepository.java
package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.ceremony.ceremony_backend.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByBadgeNumber(String badgeNumber);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    
    @Query("SELECT MAX(CAST(SUBSTRING(m.badgeNumber, 10) AS integer)) FROM Member m WHERE m.badgeNumber LIKE ?1")
    Integer findMaxBadgeNumberForYear(String yearPrefix);
}
