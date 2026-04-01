package com.ceremonie.demo.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ceremonie.demo.dto.FileUploadResult;
import com.ceremonie.demo.exceptions.FileStorageException;
import com.ceremonie.demo.services.interfaces.FileStorageService;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageServiceImpl.class);
    private final Cloudinary cloudinary;
    private final String cloudName;

    public FileStorageServiceImpl(Cloudinary cloudinary, @Value("${cloudinary.cloud-name:dvr66cxgj}") String cloudName) {
        this.cloudinary = cloudinary;
        this.cloudName = cloudName;
    }

    @Override
    public String storeFile(MultipartFile file, String directory) throws IOException {
        // Normaliser le nom du fichier
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        logger.info("Starting file upload: originalFilename={}, directory={}, fileSize={} bytes", 
                    originalFilename, directory, file.getSize());

        // Vérifier le nom du fichier
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Le nom du fichier contient une séquence de chemin invalide: " + originalFilename);
        }

        // Valider le fichier
        if (file.isEmpty()) {
            throw new FileStorageException("Le fichier est vide");
        }
        
        byte[] fileBytes = file.getBytes();
        if (fileBytes.length == 0) {
            throw new FileStorageException("Le contenu du fichier est vide");
        }
        logger.info("File bytes length: {}", fileBytes.length);

        // Générer un nom unique (sans extension pour Cloudinary)
        String uniqueFilename = UUID.randomUUID().toString();

        try {
            // Upload vers Cloudinary
            String fullPublicId = directory + "/" + uniqueFilename;
            logger.info("Uploading to Cloudinary with publicId={}, fileSize={}", fullPublicId, fileBytes.length);
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "public_id", fullPublicId,
                "resource_type", "auto"
            );

            Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes, uploadParams);
            
            String returnedPublicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");
            String url = (String) uploadResult.get("url");
            Object error = uploadResult.get("error");
            String format = (String) uploadResult.get("format");
            Object width = uploadResult.get("width");
            Object height = uploadResult.get("height");
            
            logger.info("✓ Cloudinary upload response - publicId={}, secure_url={}, url={}, format={}, error={}", 
                       returnedPublicId, secureUrl, url, format, error);
            logger.debug("Full upload response: {}", uploadResult);

            if (error != null) {
                logger.error("✗ Cloudinary upload error: {}", error);
                throw new FileStorageException("Cloudinary upload error: " + error);
            }

            if (returnedPublicId == null) {
                logger.error("✗ Cloudinary did not return a public_id. Response: {}", uploadResult);
                throw new FileStorageException("Cloudinary upload failed: no public_id in response");
            }

            // Store the secure_url for later retrieval - use as fileUrl instead of public_id
            String secureUrlForStorage = secureUrl != null ? secureUrl : String.format("https://res.cloudinary.com/%s/image/upload/%s", cloudName, returnedPublicId);
            
            logger.info("✓ File successfully uploaded to Cloudinary.");
            logger.info("  publicId: {}", returnedPublicId);
            logger.info("  secureUrl: {}", secureUrlForStorage);
            logger.info("  width: {}, height: {}", width, height);
            
            // Retourner le public_id pour le stockage en base de données
            return returnedPublicId;

        } catch (Exception ex) {
            logger.error("Error uploading file to Cloudinary", ex);
            throw new FileStorageException("Erreur lors de l'upload vers Cloudinary: " + ex.getMessage(), ex);
        }
    }

    @Override
    public FileUploadResult storeFileAdvanced(MultipartFile file, String directory) throws IOException {
        // Normaliser le nom du fichier
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        logger.info("Starting advanced file upload: originalFilename={}, directory={}, fileSize={} bytes", 
                    originalFilename, directory, file.getSize());

        // Vérifier le nom du fichier
        if (originalFilename.contains("..")) {
            throw new FileStorageException("Le nom du fichier contient une séquence de chemin invalide: " + originalFilename);
        }

        // Valider le fichier
        if (file.isEmpty()) {
            throw new FileStorageException("Le fichier est vide");
        }
        
        byte[] fileBytes = file.getBytes();
        if (fileBytes.length == 0) {
            throw new FileStorageException("Le contenu du fichier est vide");
        }
        logger.info("File bytes length: {}", fileBytes.length);

        // Générer un nom unique (sans extension pour Cloudinary)
        String uniqueFilename = UUID.randomUUID().toString();

        try {
            // Upload vers Cloudinary
            String fullPublicId = directory + "/" + uniqueFilename;
            logger.info("Uploading to Cloudinary with publicId={}, fileSize={}", fullPublicId, fileBytes.length);
            
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "public_id", fullPublicId,
                "resource_type", "image"
            );

            Map<String, Object> uploadResult = cloudinary.uploader().upload(fileBytes, uploadParams);
            
            String returnedPublicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");
            String url = (String) uploadResult.get("url");
            Object error = uploadResult.get("error");
            String format = (String) uploadResult.get("format");
            Object width = uploadResult.get("width");
            Object height = uploadResult.get("height");
            Object bytes = uploadResult.get("bytes");
            
            logger.info("✓ Cloudinary advanced upload response - publicId={}, secure_url={}", 
                       returnedPublicId, secureUrl);

            if (error != null) {
                logger.error("✗ Cloudinary upload error: {}", error);
                throw new FileStorageException("Cloudinary upload error: " + error);
            }

            if (returnedPublicId == null || secureUrl == null) {
                logger.error("✗ Cloudinary did not return required fields. Response: {}", uploadResult);
                throw new FileStorageException("Cloudinary upload failed: incomplete response");
            }

            logger.info("✓ File successfully uploaded to Cloudinary.");
            logger.info("  publicId: {}", returnedPublicId);
            logger.info("  secureUrl: {}", secureUrl);
            
            // Retourner le résultat complet d'upload
            return FileUploadResult.builder()
                .publicId(returnedPublicId)
                .secureUrl(secureUrl)
                .url(url)
                .format(format)
                .width(width != null ? ((Number)width).intValue() : null)
                .height(height != null ? ((Number)height).intValue() : null)
                .bytes(bytes != null ? ((Number)bytes).longValue() : null)
                .build();

        } catch (Exception ex) {
            logger.error("Error uploading file to Cloudinary (advanced)", ex);
            throw new FileStorageException("Erreur lors de l'upload vers Cloudinary: " + ex.getMessage(), ex);
        }
    }

    @Override
    public byte[] loadFileAsBytes(String filename, String directory) throws IOException {
        // Pour Cloudinary, on ne peut pas télécharger directement les bytes
        // Cette méthode n'est plus utilisée car les fichiers sont sur Cloudinary
        throw new UnsupportedOperationException("Les fichiers sont stockés sur Cloudinary. Utilisez getFileUrl() pour obtenir l'URL.");
    }

    @Override
    public java.nio.file.Path loadFile(String filename, String directory) {
        // Non applicable pour Cloudinary
        throw new UnsupportedOperationException("Les fichiers sont stockés sur Cloudinary.");
    }

    @Override
    public void deleteFile(String filename, String directory) {
        try {
            // filename is the full public_id (includes directory)
            Map<String, Object> deleteParams = ObjectUtils.asMap("resource_type", "image");
            cloudinary.uploader().destroy(filename, deleteParams);
        } catch (Exception ex) {
            throw new FileStorageException("Erreur lors de la suppression sur Cloudinary: " + ex.getMessage(), ex);
        }
    }

    @Override
    public String getFileUrl(String filename, String directory) {
        // Générer l'URL Cloudinary complète
        // filename is the full public_id returned from upload (already includes directory like "photos/uuid.jpg")
        // Build the complete Cloudinary URL: https://res.cloudinary.com/{cloud_name}/image/upload/{public_id}
        return String.format("https://res.cloudinary.com/%s/image/upload/%s", cloudName, filename);
    }

    @Override
    public void init() {
        // Rien à initialiser pour Cloudinary
    }
}
