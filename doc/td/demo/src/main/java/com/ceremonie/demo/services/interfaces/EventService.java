package com.ceremonie.demo.services.interfaces;


import java.time.LocalDateTime;
import java.util.List;

import com.ceremonie.demo.dto.request.CreateEventRequest;
import com.ceremonie.demo.dto.response.EventResponse;

public interface EventService {
    EventResponse createEvent(CreateEventRequest request);
    EventResponse updateEvent(Long id, CreateEventRequest request);
    EventResponse getEventById(Long id);
    List<EventResponse> getEventsByYear(Long yearId);
    List<EventResponse> getEventsByActiveYear();
    List<EventResponse> getUpcomingEvents();
    List<EventResponse> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<EventResponse> getEventsByMember(Long memberId);
    void addParticipant(Long eventId, Long memberId);
    void removeParticipant(Long eventId, Long memberId);
    void deleteEvent(Long id);
    void sendEventReminders();
}