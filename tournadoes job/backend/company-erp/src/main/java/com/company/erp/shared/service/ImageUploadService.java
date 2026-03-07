package com.company.erp.shared.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.upload-folder:erp-uploads}")
    private String uploadFolder;

    /**
     * Upload an image file to Cloudinary.
     * @param file The image file to upload
     * @param folder Optional subfolder within the main upload folder
     * @return The public URL of the uploaded image
     */
    @SuppressWarnings("unchecked")
    public String uploadImage(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String publicId = generatePublicId(folder);
            Map<String, Object> params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", uploadFolder + (folder != null ? "/" + folder : ""),
                    "resource_type", "image"
            );

            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
            String url = (String) result.get("secure_url");
            log.info("Image uploaded successfully: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Failed to upload image to Cloudinary", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to upload image: " + e.getMessage());
        }
    }

    /**
     * Upload an image file to Cloudinary with default folder.
     * @param file The image file to upload
     * @return The public URL of the uploaded image
     */
    public String uploadImage(MultipartFile file) {
        return uploadImage(file, null);
    }

    /**
     * Upload a document file to Cloudinary.
     * @param file The document file to upload
     * @param folder Optional subfolder within the main upload folder
     * @return The public URL of the uploaded document
     */
    @SuppressWarnings("unchecked")
    public String uploadDocument(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            String publicId = generatePublicId(folder);
            Map<String, Object> params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", uploadFolder + (folder != null ? "/" + folder : ""),
                    "resource_type", "raw"
            );

            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), params);
            String url = (String) result.get("secure_url");
            log.info("Document uploaded successfully: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Failed to upload document to Cloudinary", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to upload document: " + e.getMessage());
        }
    }

    /**
     * Upload a document file to Cloudinary with default folder.
     * @param file The document file to upload
     * @return The public URL of the uploaded document
     */
    public String uploadDocument(MultipartFile file) {
        return uploadDocument(file, null);
    }

    /**
     * Delete an image or document from Cloudinary.
     * @param publicId The public ID of the resource to delete
     */
    public void delete(String publicId) {
        if (publicId == null || publicId.isEmpty()) {
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Deleted resource from Cloudinary: {}", publicId);
        } catch (IOException e) {
            log.error("Failed to delete resource from Cloudinary: {}", publicId, e);
        }
    }

    /**
     * Generate a unique public ID for Cloudinary uploads.
     */
    private String generatePublicId(String folder) {
        StringBuilder sb = new StringBuilder();
        if (folder != null && !folder.isEmpty()) {
            sb.append(folder).append("/");
        }
        sb.append(UUID.randomUUID().toString());
        return sb.toString();
    }

    /**
     * Upload a QR code image (byte array) to Cloudinary.
     * @param qrCodeImage The QR code image as byte array
     * @param folder Optional subfolder within the main upload folder
     * @return The public URL of the uploaded QR code
     */
    @SuppressWarnings("unchecked")
    public String uploadQRCode(byte[] qrCodeImage, String folder) {
        if (qrCodeImage == null || qrCodeImage.length == 0) {
            return null;
        }

        try {
            String publicId = generatePublicId(folder);
            Map<String, Object> params = ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", uploadFolder + (folder != null ? "/" + folder : ""),
                    "resource_type", "image"
            );

            Map<String, Object> result = cloudinary.uploader().upload(qrCodeImage, params);
            String url = (String) result.get("secure_url");
            log.info("QR code uploaded successfully: {}", url);
            return url;
        } catch (IOException e) {
            log.error("Failed to upload QR code to Cloudinary", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to upload QR code: " + e.getMessage());
        }
    }
}

