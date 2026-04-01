package com.ceremonie.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ceremonie.demo.dto.request.CreateMemberRequest;
import com.ceremonie.demo.dto.request.UpdateMemberRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.MemberResponse;
import com.ceremonie.demo.services.interfaces.MemberService;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
@Tag(name = "Members", description = "Gestion des membres")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Créer un membre avec badge automatique")
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody CreateMemberRequest request) {
        MemberResponse member = memberService.createMember(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Membre créé avec succès", member));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Modifier un membre")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMemberRequest request) {
        MemberResponse member = memberService.updateMember(id, request);
        return ResponseEntity.ok(ApiResponse.success("Membre modifié avec succès", member));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un membre par ID")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long id) {
        MemberResponse member = memberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success("Membre trouvé", member));
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les membres")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getAllMembers() {
        List<MemberResponse> members = memberService.getAllMembers();
        return ResponseEntity.ok(ApiResponse.success("Liste des membres", members));
    }

    @GetMapping("/active")
    @Operation(summary = "Obtenir tous les membres actifs")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getActiveMembers() {
        List<MemberResponse> members = memberService.getActiveMembers();
        return ResponseEntity.ok(ApiResponse.success("Liste des membres actifs", members));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des membres")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> searchMembers(@RequestParam String query) {
        List<MemberResponse> members = memberService.searchMembers(query);
        return ResponseEntity.ok(ApiResponse.success("Résultats de recherche", members));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un membre")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok(ApiResponse.success("Membre supprimé avec succès", null));
    }
}
