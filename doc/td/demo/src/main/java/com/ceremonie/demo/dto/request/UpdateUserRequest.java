package com.ceremonie.demo.dto.request;

import com.ceremonie.demo.enums.Role;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    
    @Email(message = "L'email doit être valide")
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private Role role;
    
    private Boolean active;
}
