package com.ceremonie.demo.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ceremonie.demo.dto.FileUploadResult;
import com.ceremonie.demo.dto.request.CreateMediaRequest;
import com.ceremonie.demo.dto.response.MediaResponse;
import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Media;
import com.ceremonie.demo.entity.User;
import com.ceremonie.demo.enums.MediaType;
import com.ceremonie.demo.exceptions.FileStorageException;
import com.ceremonie.demo.exceptions.ResourceNotFoundException;
import com.ceremonie.demo.repository.CeremonialYearRepository;
import com.ceremonie.demo.repository.MediaRepository;
import com.ceremonie.demo.repository.UserRepository;
import com.ceremonie.demo.services.interfaces.FileStorageService;
import com.ceremonie.demo.services.interfaces.MediaService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final CeremonialYearRepository ceremonialYearRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/gif"
    );

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/mpeg", "video/quicktime", "video/x-msvideo"
    );

    private static final List<String> ALLOWED_AUDIO_TYPES = Arrays.asList(
            "audio/mpeg", "audio/mp3", "audio/wav", "audio/ogg"
    );

    @Override
    @Transactional
    public MediaResponse uploadMedia(MultipartFile file, CreateMediaRequest request) throws IOException {
        // Valider le fichier
        if (file.isEmpty()) {
            throw new FileStorageException("Le fichier est vide");
        }

        // Valider le type de fichier
        String contentType = file.getContentType();
        validateFileType(contentType, request.getType());

        // Trouver l'année cérémoniale
        CeremonialYear year = ceremonialYearRepository.findById(request.getCeremonialYearId())
                .orElseThrow(() -> new ResourceNotFoundException("Année cérémoniale non trouvée"));

        // Déterminer le répertoire en fonction du type
        String directory = getDirectoryForType(request.getType());

        // Stocker le fichier avec les détails complets
        FileUploadResult uploadResult = fileStorageService.storeFileAdvanced(file, directory);

        // Créer l'entité Media - utiliser le secure_url de Cloudinary
        Media media = Media.builder()
                .ceremonialYear(year)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .fileUrl(uploadResult.getSecureUrl())  // Utiliser le secure_url complet et valide
                .fileSize(file.getSize())
                .tags(request.getTags())
                .build();

        // Obtenir l'utilisateur connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            User user = userRepository.findByUsername(auth.getName()).orElse(null);
            media.setUploadedBy(user);
        }

        // Générer une miniature pour les vidéos (optionnel)
        if (request.getType() == MediaType.VIDEO) {
            // TODO: Implémenter la génération de miniatures
            media.setThumbnailUrl(uploadResult.getSecureUrl()); // Utiliser le secure_url
        } else if (request.getType() == MediaType.PHOTO) {
            media.setThumbnailUrl(uploadResult.getSecureUrl()); // Utiliser le secure_url
        }

        Media savedMedia = mediaRepository.save(media);
        return mapToResponse(savedMedia);
    }

    @Override
    public MediaResponse getMediaById(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé"));
        return mapToResponse(media);
    }

    @Override
    public List<MediaResponse> getMediasByYear(Long yearId) {
        return mediaRepository.findByCeremonialYearId(yearId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediasByActiveYear() {
        return mediaRepository.findByActiveCeremonialYear().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> getMediasByType(MediaType type) {
        return mediaRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<MediaResponse> searchMedias(String search) {
        return mediaRepository.searchMedias(search).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadMedia(Long id) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé"));

        // For Cloudinary, we can't directly download bytes from the service
        // The download should be handled by redirecting to the Cloudinary URL
        // This method is kept for compatibility but will throw an exception
        throw new UnsupportedOperationException("Les fichiers sont stockés sur Cloudinary. Utilisez l'URL directe pour le téléchargement.");
    }

    @Override
    @Transactional
    public void deleteMedia(Long id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Média non trouvé"));

        // Supprimer le fichier physique
        String directory = getDirectoryForType(media.getType());
        String filename = extractFilenameFromUrl(media.getFileUrl());
        fileStorageService.deleteFile(filename, directory);

        // Supprimer l'entité
        media.setDeleted(true);
        mediaRepository.save(media);
    }

    private void validateFileType(String contentType, MediaType mediaType) {
        boolean isValid = false;

        switch (mediaType) {
            case PHOTO:
                isValid = ALLOWED_IMAGE_TYPES.contains(contentType);
                break;
            case VIDEO:
                isValid = ALLOWED_VIDEO_TYPES.contains(contentType);
                break;
            case AUDIO:
                isValid = ALLOWED_AUDIO_TYPES.contains(contentType);
                break;
        }

        if (!isValid) {
            throw new FileStorageException("Type de fichier non autorisé pour " + mediaType + ": " + contentType);
        }
    }

    private String getDirectoryForType(MediaType type) {
        switch (type) {
            case PHOTO:
                return "photos";
            case VIDEO:
                return "videos";
            case AUDIO:
                return "audios";
            default:
                return "medias";
        }
    }

    private String extractFilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private String cleanCloudinaryUrl(String url) {
        if (url == null) return null;
        
        // Fix duplicate directory paths in Cloudinary URLs
        // e.g., v1/photos/photos/photos/filename -> v1/photos/filename
        if (url.contains("image/upload/")) {
            String[] parts = url.split("image/upload/");
            if (parts.length == 2) {
                String uploadPart = parts[1];
                String[] segments = uploadPart.split("/");
                
                // Remove duplicate folder names (keep only the last occurrence)
                if (segments.length >= 3) {
                    String folderName = segments[1]; // e.g., "photos"
                    
                    // Count consecutive duplicates of this folder
                    int lastIndex = 1;
                    for (int i = 2; i < segments.length; i++) {
                        if (segments[i].equals(folderName)) {
                            lastIndex = i;
                        } else {
                            break;
                        }
                    }
                    
                    // Reconstruct URL with only one instance of the folder
                    StringBuilder cleanUrl = new StringBuilder(parts[0]);
                    cleanUrl.append("image/upload/");
                    cleanUrl.append(segments[0]).append("/").append(segments[1]).append("/");
                    
                    for (int i = lastIndex + 1; i < segments.length; i++) {
                        cleanUrl.append(segments[i]);
                        if (i < segments.length - 1) {
                            cleanUrl.append("/");
                        }
                    }
                    
                    return cleanUrl.toString();
                }
            }
        }
        
        return url;
    }

    private MediaResponse mapToResponse(Media media) {
        return MediaResponse.builder()
                .id(media.getId())
                .ceremonialYearId(media.getCeremonialYear().getId())
                .ceremonialYear(media.getCeremonialYear().getYear())
                .title(media.getTitle())
                .description(media.getDescription())
                .type(media.getType())
                .fileUrl(cleanCloudinaryUrl(media.getFileUrl()))
                .thumbnailUrl(cleanCloudinaryUrl(media.getThumbnailUrl()))
                .fileSize(media.getFileSize())
                .duration(media.getDuration())
                .uploadedByName(media.getUploadedBy() != null ?
                        media.getUploadedBy().getFirstName() + " " + media.getUploadedBy().getLastName() : null)
                .tags(media.getTags())
                .createdAt(media.getCreatedAt())
                .build();
    }
}
