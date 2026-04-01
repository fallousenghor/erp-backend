package com.ceremonie.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ceremonie.demo.enums.EventType;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {
    
    @ManyToOne
    @JoinColumn(name = "ceremonial_year_id", nullable = false)
    private CeremonialYear ceremonialYear;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType type;
    
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "address", columnDefinition = "TEXT")
    private String address;
    
    @ManyToMany
    @JoinTable(
        name = "event_participants",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<Member> participants = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private User organizer;
    
    @Column(name = "reminder_sent")
    private Boolean reminderSent = false;
    
    @Column(name = "reminder_date")
    private LocalDateTime reminderDate;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
}