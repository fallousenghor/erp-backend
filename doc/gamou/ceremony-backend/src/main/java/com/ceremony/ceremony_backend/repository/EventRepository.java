// repository/EventRepository.java
package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceremony.ceremony_backend.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEditionId(Long editionId);
    List<Event> findByStartDateTimeBetween(LocalDateTime start, LocalDateTime end);
}