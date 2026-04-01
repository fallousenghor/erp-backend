package com.ceremonie.demo.dto.request;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {
    
    private String firstName;
    
    private String lastName;
    
    @Email(message = "L'email doit être valide")
    private String email;
    
    private String phoneNumber;
    
    private String secondaryPhone;
    
    private LocalDate dateOfBirth;
    
    private String address;
    
    private Boolean active;
    
    private String emergencyContact;
    
    private String emergencyPhone;
    
    private String notes;
}