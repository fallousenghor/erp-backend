package com.ceremonie.demo.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberRequest {
    
    @NotBlank(message = "Le prénom est obligatoire")
    private String firstName;
    
    @NotBlank(message = "Le nom est obligatoire")
    private String lastName;
    
    @Email(message = "L'email doit être valide")
    private String email;
    
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String phoneNumber;
    
    private String secondaryPhone;
    
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateOfBirth;
    
    private String address;
    
    private String emergencyContact;
    
    private String emergencyPhone;
    
    private String notes;
}