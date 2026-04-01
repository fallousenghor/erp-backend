package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.ceremonie.demo.enums.EventType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private Long ceremonialYearId;
    private Integer ceremonialYear;
    private String title;
    private String description;
    private EventType type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private String address;
    private List<MemberResponse> participants;
    private String organizerName;
    private Boolean reminderSent;
    private LocalDateTime reminderDate;
}