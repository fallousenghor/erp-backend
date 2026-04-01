package com.ceremonie.demo.controllers;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ceremonie.demo.dto.request.CreateEventRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.EventResponse;
import com.ceremonie.demo.services.interfaces.EventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Gestion des événements")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Créer un événement")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(@Valid @RequestBody CreateEventRequest request) {
        EventResponse event = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Événement créé avec succès", event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Modifier un événement")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody CreateEventRequest request) {
        EventResponse event = eventService.updateEvent(id, request);
        return ResponseEntity.ok(ApiResponse.success("Événement modifié avec succès", event));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un événement par ID")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable Long id) {
        EventResponse event = eventService.getEventById(id);
        return ResponseEntity.ok(ApiResponse.success("Événement trouvé", event));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Obtenir les événements à venir")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getUpcomingEvents() {
        List<EventResponse> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(ApiResponse.success("Événements à venir", events));
    }

    @GetMapping("/active-year")
    @Operation(summary = "Obtenir les événements de l'année active")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByActiveYear() {
        List<EventResponse> events = eventService.getEventsByActiveYear();
        return ResponseEntity.ok(ApiResponse.success("Événements de l'année active", events));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Obtenir les événements d'un membre")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getEventsByMember(@PathVariable Long memberId) {
        List<EventResponse> events = eventService.getEventsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("Événements du membre", events));
    }

    @PostMapping("/{eventId}/participants/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Ajouter un participant à un événement")
    public ResponseEntity<ApiResponse<Void>> addParticipant(
            @PathVariable Long eventId,
            @PathVariable Long memberId) {
        eventService.addParticipant(eventId, memberId);
        return ResponseEntity.ok(ApiResponse.success("Participant ajouté", null));
    }

    @DeleteMapping("/{eventId}/participants/{memberId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Retirer un participant d'un événement")
    public ResponseEntity<ApiResponse<Void>> removeParticipant(
            @PathVariable Long eventId,
            @PathVariable Long memberId) {
        eventService.removeParticipant(eventId, memberId);
        return ResponseEntity.ok(ApiResponse.success("Participant retiré", null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un événement")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok(ApiResponse.success("Événement supprimé", null));
    }
}