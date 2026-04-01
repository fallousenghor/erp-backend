package com.ceremonie.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByMemberNumber(String memberNumber);
    
    Optional<Member> findByEmail(String email);
    
    Optional<Member> findByPhoneNumber(String phoneNumber);
    
    Boolean existsByMemberNumber(String memberNumber);
    
    Boolean existsByEmail(String email);
    
    Boolean existsByPhoneNumber(String phoneNumber);
    
    List<Member> findByActiveTrue();
    
    List<Member> findByActiveFalse();
    
    @Query("SELECT m FROM Member m WHERE m.deleted = false AND m.active = true ORDER BY m.lastName, m.firstName")
    List<Member> findAllActiveMembers();
    
    @Query("SELECT m FROM Member m WHERE m.deleted = false ORDER BY m.registrationDate DESC")
    List<Member> findAllOrderByRegistrationDateDesc();
    
    @Query("SELECT m FROM Member m WHERE " +
           "(LOWER(m.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "m.memberNumber LIKE CONCAT('%', :search, '%') OR " +
           "m.phoneNumber LIKE CONCAT('%', :search, '%')) AND " +
           "m.deleted = false")
    List<Member> searchMembers(@Param("search") String search);
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.active = true AND m.deleted = false")
    Long countActiveMembers();
    
    @Query("SELECT COUNT(m) FROM Member m WHERE m.registrationDate >= :startDate AND m.deleted = false")
    Long countMembersRegisteredAfter(@Param("startDate") LocalDate startDate);
    
    @Query("SELECT m FROM Member m WHERE m.registrationDate BETWEEN :startDate AND :endDate AND m.deleted = false")
    List<Member> findMembersRegisteredBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );
}