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

import com.ceremonie.demo.dto.request.CreateContributionRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.ContributionResponse;
import com.ceremonie.demo.services.interfaces.ContributionService;

import java.util.List;

@RestController
@RequestMapping("/api/contributions")
@RequiredArgsConstructor
@Tag(name = "Contributions", description = "Gestion des cotisations")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class ContributionController {

    private final ContributionService contributionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Créer une cotisation")
    public ResponseEntity<ApiResponse<ContributionResponse>> createContribution(
            @Valid @RequestBody CreateContributionRequest request) {
        ContributionResponse contribution = contributionService.createContribution(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Cotisation créée", contribution));
    }

    @GetMapping
    @Operation(summary = "Obtenir toutes les cotisations")
    public ResponseEntity<ApiResponse<List<ContributionResponse>>> getAllContributions() {
        List<ContributionResponse> contributions = contributionService.getAllContributions();
        return ResponseEntity.ok(ApiResponse.success("Liste des cotisations", contributions));
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Cotisations d'un membre")
    public ResponseEntity<ApiResponse<List<ContributionResponse>>> getContributionsByMember(@PathVariable Long memberId) {
        List<ContributionResponse> contributions = contributionService.getContributionsByMember(memberId);
        return ResponseEntity.ok(ApiResponse.success("Cotisations du membre", contributions));
    }

    @GetMapping("/unpaid")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Cotisations impayées")
    public ResponseEntity<ApiResponse<List<ContributionResponse>>> getUnpaidContributions() {
        List<ContributionResponse> contributions = contributionService.getUnpaidContributions();
        return ResponseEntity.ok(ApiResponse.success("Cotisations impayées", contributions));
    }

    @PutMapping("/{contributionId}/mark-paid/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRESORIER')")
    @Operation(summary = "Marquer comme payée")
    public ResponseEntity<ApiResponse<Void>> markAsPaid(
            @PathVariable Long contributionId,
            @PathVariable Long transactionId) {
        contributionService.markAsPaid(contributionId, transactionId);
        return ResponseEntity.ok(ApiResponse.success("Cotisation payée", null));
    }
}
