package com.ceremonie.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ceremonie.demo.services.interfaces.FileStorageService;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "Accès aux fichiers uploadés")
@CrossOrigin(origins = "*")
public class FileController {

    private final FileStorageService fileStorageService;

    @GetMapping("/{directory}/{filename:.+}")
    @Operation(summary = "Récupérer un fichier")
    public ResponseEntity<Resource> getFile(
            @PathVariable String directory,
            @PathVariable String filename) throws IOException {
        
        Path filePath = fileStorageService.loadFile(filename, directory);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("Fichier non trouvé: " + filename);
        }

        // Déterminer le type de contenu
        String contentType = determineContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    private String determineContentType(String filename) {
        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "mp4":
                return "video/mp4";
            case "mp3":
                return "audio/mpeg";
            case "pdf":
                return "application/pdf";
            default:
                return "application/octet-stream";
        }
    }
}

