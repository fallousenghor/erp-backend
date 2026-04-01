package com.ceremonie.demo.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ceremonie.demo.dto.request.CreateMediaRequest;
import com.ceremonie.demo.dto.response.ApiResponse;
import com.ceremonie.demo.dto.response.MediaResponse;
import com.ceremonie.demo.enums.MediaType;
import com.ceremonie.demo.services.interfaces.MediaService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/medias")
@RequiredArgsConstructor
@Tag(name = "Medias", description = "Gestion des médias (photos, vidéos, sons)")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(consumes = org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SECRETAIRE')")
    @Operation(summary = "Upload un média", description = "Upload photo, vidéo ou audio")
    public ResponseEntity<ApiResponse<MediaResponse>> uploadMedia(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") @Valid CreateMediaRequest request) throws IOException {
        MediaResponse media = mediaService.uploadMedia(file, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Média uploadé avec succès", media));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un média par ID")
    public ResponseEntity<ApiResponse<MediaResponse>> getMediaById(@PathVariable Long id) {
        MediaResponse media = mediaService.getMediaById(id);
        return ResponseEntity.ok(ApiResponse.success("Média trouvé", media));
    }

    @GetMapping("/year/{yearId}")
    @Operation(summary = "Obtenir les médias par année")
    public ResponseEntity<ApiResponse<List<MediaResponse>>> getMediasByYear(@PathVariable Long yearId) {
        List<MediaResponse> medias = mediaService.getMediasByYear(yearId);
        return ResponseEntity.ok(ApiResponse.success("Médias de l'année", medias));
    }

    @GetMapping("/active-year")
    @Operation(summary = "Obtenir les médias de l'année active")
    public ResponseEntity<ApiResponse<List<MediaResponse>>> getMediasByActiveYear() {
        List<MediaResponse> medias = mediaService.getMediasByActiveYear();
        return ResponseEntity.ok(ApiResponse.success("Médias de l'année active", medias));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Obtenir les médias par type")
    public ResponseEntity<ApiResponse<List<MediaResponse>>> getMediasByType(@PathVariable MediaType type) {
        List<MediaResponse> medias = mediaService.getMediasByType(type);
        return ResponseEntity.ok(ApiResponse.success("Médias de type " + type, medias));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des médias")
    public ResponseEntity<ApiResponse<List<MediaResponse>>> searchMedias(@RequestParam String query) {
        List<MediaResponse> medias = mediaService.searchMedias(query);
        return ResponseEntity.ok(ApiResponse.success("Résultats de recherche", medias));
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Télécharger un média")
    public ResponseEntity<Void> downloadMedia(@PathVariable Long id) {
        MediaResponse media = mediaService.getMediaById(id);

        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, media.getFileUrl())
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer un média")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(@PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok(ApiResponse.success("Média supprimé avec succès", null));
    }
}