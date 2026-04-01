package com.ceremonie.demo.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremonie.demo.dto.request.CreateEventRequest;
import com.ceremonie.demo.dto.response.EventResponse;
import com.ceremonie.demo.dto.response.MemberResponse;
import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Event;
import com.ceremonie.demo.entity.Member;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.EventRepository;
import com.ceremonie.demo.repository.MemberRepository;
import com.ceremonie.demo.services.interfaces.EventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CeremonialYearRepository ceremonialYearRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {
        CeremonialYear year = ceremonialYearRepository.findById(request.getCeremonialYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));

        Event event = Event.builder()
                .ceremonialYear(year)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .location(request.getLocation())
                .address(request.getAddress())
                .reminderDate(request.getReminderDate())
                .reminderSent(false)
                .notes(request.getNotes())
                .participants(new ArrayList<>())
                .build();

        // Ajouter les participants
        if (request.getParticipantIds() != null && !request.getParticipantIds().isEmpty()) {
            List<Member> participants = memberRepository.findAllById(request.getParticipantIds());
            event.setParticipants(participants);
        }

        Event savedEvent = eventRepository.save(event);
        return mapToResponse(savedEvent);
    }

    @Override
    @Transactional
    public EventResponse updateEvent(Long id, CreateEventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé"));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setType(request.getType());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setLocation(request.getLocation());
        event.setAddress(request.getAddress());
        event.setReminderDate(request.getReminderDate());
        event.setNotes(request.getNotes());

        if (request.getParticipantIds() != null) {
            List<Member> participants = memberRepository.findAllById(request.getParticipantIds());
            event.setParticipants(participants);
        }

        Event updatedEvent = eventRepository.save(event);
        return mapToResponse(updatedEvent);
    }

    @Override
    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé"));
        return mapToResponse(event);
    }

    @Override
    public List<EventResponse> getEventsByYear(Long yearId) {
        return eventRepository.findByCeremonialYearId(yearId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getEventsByActiveYear() {
        return eventRepository.findByActiveCeremonialYear().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getUpcomingEvents() {
        return eventRepository.findUpcomingEvents().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getEventsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return eventRepository.findByDateRange(startDate, endDate).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventResponse> getEventsByMember(Long memberId) {
        return eventRepository.findEventsByMemberId(memberId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addParticipant(Long eventId, Long memberId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Membre non trouvé"));

        if (!event.getParticipants().contains(member)) {
            event.getParticipants().add(member);
            eventRepository.save(event);
        }
    }

    @Override
    @Transactional
    public void removeParticipant(Long eventId, Long memberId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé"));
        event.getParticipants().removeIf(member -> member.getId().equals(memberId));
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Événement non trouvé"));
        event.setDeleted(true);
        eventRepository.save(event);
    }

    @Override
    @Transactional
    public void sendEventReminders() {
        List<Event> eventsNeedingReminder = eventRepository.findEventsNeedingReminder();
        for (Event event : eventsNeedingReminder) {
            // TODO: Implémenter l'envoi de notifications (email/push)
            event.setReminderSent(true);
            eventRepository.save(event);
        }
    }

    private EventResponse mapToResponse(Event event) {
        List<MemberResponse> participants = event.getParticipants().stream()
                .map(member -> MemberResponse.builder()
                        .id(member.getId())
                        .memberNumber(member.getMemberNumber())
                        .firstName(member.getFirstName())
                        .lastName(member.getLastName())
                        .phoneNumber(member.getPhoneNumber())
                        .build())
                .collect(Collectors.toList());

        return EventResponse.builder()
                .id(event.getId())
                .ceremonialYearId(event.getCeremonialYear().getId())
                .ceremonialYear(event.getCeremonialYear().getYear())
                .title(event.getTitle())
                .description(event.getDescription())
                .type(event.getType())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .location(event.getLocation())
                .address(event.getAddress())
                .participants(participants)
                .organizerName(event.getOrganizer() != null ? 
                        event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName() : null)
                .reminderSent(event.getReminderSent())
                .reminderDate(event.getReminderDate())
                .build();
    }
}