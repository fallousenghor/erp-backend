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

import com.ceremonie.demo.dto.request.CreateCeremonialYearRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.CeremonialYearResponse;
import com.ceremonie.demo.services.interfaces.CeremonialYearService;

import java.util.List;

@RestController
@RequestMapping("/api/ceremonial-years")
@RequiredArgsConstructor
@Tag(name = "Ceremonial Years", description = "Gestion des années cérémoniales")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class CeremonialYearController {

    private final CeremonialYearService ceremonialYearService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer une année cérémoniale")
    public ResponseEntity<ApiResponse<CeremonialYearResponse>> createCeremonialYear(
            @Valid @RequestBody CreateCeremonialYearRequest request) {
        CeremonialYearResponse year = ceremonialYearService.createCeremonialYear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Année créée avec succès", year));
    }

    @GetMapping("/active")
    @Operation(summary = "Obtenir l'année active")
    public ResponseEntity<ApiResponse<CeremonialYearResponse>> getActiveCeremonialYear() {
        CeremonialYearResponse year = ceremonialYearService.getActiveCeremonialYear();
        return ResponseEntity.ok(ApiResponse.success("Année active", year));
    }

    @GetMapping
    @Operation(summary = "Obtenir toutes les années")
    public ResponseEntity<ApiResponse<List<CeremonialYearResponse>>> getAllCeremonialYears() {
        List<CeremonialYearResponse> years = ceremonialYearService.getAllCeremonialYears();
        return ResponseEntity.ok(ApiResponse.success("Liste des années", years));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activer une année (désactive les autres)")
    public ResponseEntity<ApiResponse<Void>> activateCeremonialYear(@PathVariable Long id) {
        ceremonialYearService.activateCeremonialYear(id);
        return ResponseEntity.ok(ApiResponse.success("Année activée avec succès", null));
    }
}
