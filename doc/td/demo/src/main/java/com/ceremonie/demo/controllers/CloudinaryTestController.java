package com.ceremonie.demo.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary-test")
@RequiredArgsConstructor
@Tag(name = "Cloudinary Test", description = "Test d'upload Cloudinary")
@CrossOrigin(origins = "*")
public class CloudinaryTestController {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryTestController.class);
    private final Cloudinary cloudinary;

    @PostMapping("/test-upload")
    @Operation(summary = "Tester l'upload Cloudinary directement")
    public ResponseEntity<?> testUpload(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("=== CLOUDINARY TEST UPLOAD START ===");
            logger.info("File: {}, Size: {} bytes, ContentType: {}", 
                       file.getOriginalFilename(), file.getSize(), file.getContentType());

            byte[] fileBytes = file.getBytes();
            logger.info("File bytes read: {} bytes", fileBytes.length);

            // Test 1: Simple upload without public_id
            logger.info("\n--- Test 1: Simple upload without public_id ---");
            Map<String, Object> result1 = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                "resource_type", "auto"
            ));
            logger.info("Result 1: {}", result1);

            // Test 2: Upload with public_id in photos folder
            logger.info("\n--- Test 2: Upload with public_id in photos folder ---");
            String publicId = "photos/test-" + System.currentTimeMillis();
            Map<String, Object> result2 = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                "public_id", publicId,
                "resource_type", "auto"
            ));
            logger.info("Result 2: {}", result2);

            // Test 3: Test with create_folder = true
            logger.info("\n--- Test 3: Upload with create_folder = true ---");
            String publicId3 = "photos/test-folder-" + System.currentTimeMillis();
            Map<String, Object> result3 = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                "public_id", publicId3,
                "resource_type", "auto",
                "create_folder", true
            ));
            logger.info("Result 3: {}", result3);

            logger.info("\n=== CLOUDINARY TEST UPLOAD END ===");

            return ResponseEntity.ok(Map.of(
                "test1", result1,
                "test2", result2,
                "test3", result3,
                "message", "Tests completed - check logs for details"
            ));

        } catch (IOException e) {
            logger.error("❌ IOException during test: ", e);
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "type", "IOException"
            ));
        } catch (Exception e) {
            logger.error("❌ Exception during test: ", e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", e.getMessage(),
                "type", e.getClass().getSimpleName(),
                "cause", e.getCause() != null ? e.getCause().getMessage() : "No cause"
            ));
        }
    }

    @GetMapping("/info")
    @Operation(summary = "Afficher les infos de connexion Cloudinary")
    public ResponseEntity<?> getCloudinaryInfo() {
        try {
            // This will show configured credentials (without revealing the secret)
            return ResponseEntity.ok(Map.of(
                "configured", true,
                "message", "Cloudinary is configured. Check application logs for upload tests."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "configured", false,
                "error", e.getMessage()
            ));
        }
    }
}
