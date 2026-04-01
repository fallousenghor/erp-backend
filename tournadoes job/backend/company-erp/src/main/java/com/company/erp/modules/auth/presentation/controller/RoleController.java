package com.company.erp.modules.auth.presentation.controller;

import com.company.erp.modules.auth.domain.model.Role;
import com.company.erp.modules.auth.domain.repository.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Roles Controller - RBAC management
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Roles & Permissions", description = "RBAC management")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    @GetMapping("/roles")
    @Operation(summary = "List all roles")
    public ResponseEntity<List<Role>> getRoles() {
        // Use findAll from JpaRepository through implementation
        return ResponseEntity.ok(roleRepository.findAll());
    }

    @GetMapping("/roles/{id}")
    @Operation(summary = "Get role by ID")
    public ResponseEntity<Role> getRole(@PathVariable UUID id) {
        return roleRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/roles")
    @Operation(summary = "Create new role")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role savedRole = roleRepository.save(role);
        return ResponseEntity.status(201).body(savedRole);
    }

    @PutMapping("/roles/{id}")
    @Operation(summary = "Update role")
    public ResponseEntity<Role> updateRole(
            @PathVariable UUID id,
            @RequestBody Role role) {
        
        return roleRepository.findById(id)
            .map(existingRole -> {
                role.setId(id);
                Role updated = roleRepository.save(role);
                return ResponseEntity.ok(updated);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Delete role")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id) {
        roleRepository.findById(id).ifPresent(roleRepository::delete);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/roles/assign")
    @Operation(summary = "Assign role to user")
    public ResponseEntity<Void> assignRole(
            @RequestParam String userId,
            @RequestParam String roleId) {
        // Implementation to assign role to user
        return ResponseEntity.ok().build();
    }

    @GetMapping("/roles/stats")
    @Operation(summary = "Get role statistics")
    public ResponseEntity<Map<String, Object>> getRoleStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRoles", 6); // Hardcoded for now
        stats.put("totalUsers", 0);
        stats.put("adminUsers", 0);
        stats.put("customRoles", 0);
        return ResponseEntity.ok(stats);
    }
}
