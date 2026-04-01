package com.ceremonie.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponse {
    private Long id;
    private String memberNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String secondaryPhone;
    private LocalDate dateOfBirth;
    private String address;
    private LocalDate registrationDate;
    private String photoUrl;
    private Boolean active;
    private String emergencyContact;
    private String emergencyPhone;
    private BadgeResponse badge;
    private LocalDateTime createdAt;
}
