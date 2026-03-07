package com.company.erp.modules.inventory.presentation.controller;

import com.company.erp.modules.inventory.application.dto.request.AssignAssetRequest;
import com.company.erp.modules.inventory.application.dto.request.CreateAssetRequest;
import com.company.erp.modules.inventory.application.dto.request.CreateAssetRequestWithMedia;
import com.company.erp.modules.inventory.application.dto.response.AssetResponse;
import com.company.erp.modules.inventory.application.service.AssetService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import com.company.erp.shared.service.ImageUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/v1/assets")
@Tag(name = "Assets", description = "Inventory — Asset management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;
    private final ImageUploadService imageUploadService;

    @PostMapping
    @Operation(summary = "Register a new asset")
    public ResponseEntity<ApiResponse<AssetResponse>> create(
            @Valid @RequestBody CreateAssetRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(assetService.create(request)));
    }

    @PostMapping(value = "/with-media", consumes = {"multipart/form-data"})
    @Operation(summary = "Register a new asset with image and document")
    public ResponseEntity<ApiResponse<AssetResponse>> createWithMedia(
            @Valid @ModelAttribute CreateAssetRequestWithMedia request) {
        
        // Upload image to Cloudinary if provided
        String imageUrl = null;
        if (request.image() != null && !request.image().isEmpty()) {
            imageUrl = imageUploadService.uploadImage(request.image(), "assets");
        }
        
        // Upload document to Cloudinary if provided
        String documentUrl = null;
        if (request.document() != null && !request.document().isEmpty()) {
            documentUrl = imageUploadService.uploadDocument(request.document(), "assets");
        }
        
        return ResponseEntity.status(201)
                .body(ApiResponse.created(assetService.createWithMedia(request, imageUrl, documentUrl)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID")
    public ResponseEntity<ApiResponse<AssetResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(assetService.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List assets with filtering")
    public ResponseEntity<ApiResponse<PageResponse<AssetResponse>>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                assetService.findAll(name, status, category, PageRequest.of(page, size))));
    }

    @PostMapping("/{id}/assign")
    @Operation(summary = "Assign asset to an employee")
    public ResponseEntity<ApiResponse<AssetResponse>> assign(
            @PathVariable UUID id,
            @Valid @RequestBody AssignAssetRequest request) {
        return ResponseEntity.ok(ApiResponse.success(assetService.assign(id, request)));
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Return an assigned asset")
    public ResponseEntity<ApiResponse<AssetResponse>> returnAsset(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(assetService.returnAsset(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update asset information")
    public ResponseEntity<ApiResponse<AssetResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateAssetRequest request) {
        return ResponseEntity.ok(ApiResponse.success(assetService.update(id, request)));
    }

    @PutMapping(value = "/{id}/media", consumes = {"multipart/form-data"})
    @Operation(summary = "Update asset image and document")
    public ResponseEntity<ApiResponse<AssetResponse>> updateMedia(
            @PathVariable UUID id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "document", required = false) MultipartFile document) {
        
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageUploadService.uploadImage(image, "assets");
        }
        
        String documentUrl = null;
        if (document != null && !document.isEmpty()) {
            documentUrl = imageUploadService.uploadDocument(document, "assets");
        }
        
        return ResponseEntity.ok(ApiResponse.success(assetService.updateMedia(id, imageUrl, documentUrl)));
    }
}
