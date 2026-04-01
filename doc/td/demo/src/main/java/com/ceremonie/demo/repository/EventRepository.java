package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Event;
import com.ceremonie.demo.enums.EventType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByCeremonialYear(CeremonialYear ceremonialYear);
    
    List<Event> findByType(EventType type);
    
    @Query("SELECT e FROM Event e WHERE e.ceremonialYear.id = :yearId AND e.deleted = false ORDER BY e.startDate DESC")
    List<Event> findByCeremonialYearId(@Param("yearId") Long yearId);
    
    @Query("SELECT e FROM Event e WHERE e.ceremonialYear.active = true AND e.deleted = false ORDER BY e.startDate ASC")
    List<Event> findByActiveCeremonialYear();
    
    @Query("SELECT e FROM Event e WHERE " +
           "e.startDate >= :startDate AND " +
           "e.startDate <= :endDate AND " +
           "e.deleted = false ORDER BY e.startDate ASC")
    List<Event> findByDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT e FROM Event e WHERE " +
           "e.startDate > CURRENT_TIMESTAMP AND " +
           "e.deleted = false ORDER BY e.startDate ASC")
    List<Event> findUpcomingEvents();
    
    @Query("SELECT e FROM Event e WHERE " +
           "e.startDate < CURRENT_TIMESTAMP AND " +
           "e.deleted = false ORDER BY e.startDate DESC")
    List<Event> findPastEvents();
    
    @Query("SELECT e FROM Event e JOIN e.participants p WHERE " +
           "p.id = :memberId AND e.deleted = false ORDER BY e.startDate DESC")
    List<Event> findEventsByMemberId(@Param("memberId") Long memberId);
    
    @Query("SELECT e FROM Event e WHERE " +
           "e.reminderSent = false AND " +
           "e.startDate > CURRENT_TIMESTAMP AND " +
           "e.reminderDate <= CURRENT_TIMESTAMP AND " +
           "e.deleted = false")
    List<Event> findEventsNeedingReminder();
    
    @Query("SELECT COUNT(e) FROM Event e WHERE " +
           "e.ceremonialYear.id = :yearId AND e.deleted = false")
    Long countEventsByYear(@Param("yearId") Long yearId);
}