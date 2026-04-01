package com.ceremony.ceremony_backend.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ceremony.ceremony_backend.dto.request.CreateTreasurerRequest;
import com.ceremony.ceremony_backend.dto.response.ApiResponse;
import com.ceremony.ceremony_backend.entity.User;
import com.ceremony.ceremony_backend.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/treasurer")
    public ResponseEntity<User> createTreasurer(@Valid @RequestBody CreateTreasurerRequest request) {
        User treasurer = userService.createTreasurer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(treasurer);
    }
    
    @GetMapping("/treasurers")
    public ResponseEntity<List<User>> getAllTreasurers() {
        List<User> treasurers = userService.getAllTreasurers();
        return ResponseEntity.ok(treasurers);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    @PatchMapping("/{id}/disable")
    public ResponseEntity<ApiResponse> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "Utilisateur désactivé"));
    }
    
    @PatchMapping("/{id}/enable")
    public ResponseEntity<ApiResponse> enableUser(@PathVariable Long id) {
        userService.enableUser(id);
        return ResponseEntity.ok(new ApiResponse(true, "Utilisateur activé"));
    }
}