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

import com.ceremonie.demo.dto.request.CreateMaterialRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.MaterialResponse;
import com.ceremonie.demo.services.interfaces.MaterialService;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Tag(name = "Materials", description = "Gestion du matériel")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Créer un matériel")
    public ResponseEntity<ApiResponse<MaterialResponse>> createMaterial(@Valid @RequestBody CreateMaterialRequest request) {
        MaterialResponse material = materialService.createMaterial(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Matériel créé avec succès", material));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Modifier un matériel")
    public ResponseEntity<ApiResponse<MaterialResponse>> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody CreateMaterialRequest request) {
        MaterialResponse material = materialService.updateMaterial(id, request);
        return ResponseEntity.ok(ApiResponse.success("Matériel modifié avec succès", material));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un matériel par ID")
    public ResponseEntity<ApiResponse<MaterialResponse>> getMaterialById(@PathVariable Long id) {
        MaterialResponse material = materialService.getMaterialById(id);
        return ResponseEntity.ok(ApiResponse.success("Matériel trouvé", material));
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les matériels")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getAllMaterials() {
        List<MaterialResponse> materials = materialService.getAllMaterials();
        return ResponseEntity.ok(ApiResponse.success("Liste des matériels", materials));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des matériels")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> searchMaterials(@RequestParam String query) {
        List<MaterialResponse> materials = materialService.searchMaterials(query);
        return ResponseEntity.ok(ApiResponse.success("Résultats de recherche", materials));
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Obtenir les matériels en stock bas")
    public ResponseEntity<ApiResponse<List<MaterialResponse>>> getLowStockMaterials(
            @RequestParam(defaultValue = "5") Integer threshold) {
        List<MaterialResponse> materials = materialService.getLowStockMaterials(threshold);
        return ResponseEntity.ok(ApiResponse.success("Matériels en stock bas", materials));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un matériel")
    public ResponseEntity<ApiResponse<Void>> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.ok(ApiResponse.success("Matériel supprimé avec succès", null));
    }
}
