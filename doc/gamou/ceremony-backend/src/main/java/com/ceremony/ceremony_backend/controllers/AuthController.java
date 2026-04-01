package com.ceremony.ceremony_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ceremony.ceremony_backend.dto.request.ChangePasswordRequest;
import com.ceremony.ceremony_backend.dto.request.LoginRequest;
import com.ceremony.ceremony_backend.dto.response.ApiResponse;
import com.ceremony.ceremony_backend.dto.response.LoginResponse;
import com.ceremony.ceremony_backend.services.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        User currentUser = authService.getCurrentUser();
        authService.changePassword(currentUser.getId(), request);
        return ResponseEntity.ok(new ApiResponse(true, "Mot de passe changé avec succès"));
    }
    
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        User user = authService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
}