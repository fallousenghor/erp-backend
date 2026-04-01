// dto/request/CreateTreasurerRequest.java
package com.ceremony.ceremony_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTreasurerRequest {
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;
}