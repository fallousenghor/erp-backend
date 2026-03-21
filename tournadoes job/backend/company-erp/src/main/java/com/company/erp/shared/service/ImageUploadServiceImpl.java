package com.company.erp.shared.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadServiceImpl implements ImageUploadService {

    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile image, String context) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String publicId = String.format("%s/%s_%s", context, image.getOriginalFilename(), timestamp);
            
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                Map.of(
                    "public_id", publicId,
                    "resource_type", "image",
                    "quality", "auto",
                    "fetch_format", "auto"
                ));

            @SuppressWarnings("unchecked")
            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploaded to {}: {}", context, secureUrl);
            return secureUrl;
            
        } catch (IOException e) {
            log.error("Failed to upload image to {}: {}", context, e.getMessage());
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public String uploadDocument(MultipartFile document, String context) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String publicId = String.format("%s/%s_%s", context, document.getOriginalFilename(), timestamp);
            
            Map uploadResult = cloudinary.uploader().upload(document.getBytes(),
                Map.of(
                    "public_id", publicId,
                    "resource_type", "raw",
                    "filename_override", document.getOriginalFilename()
                ));

            @SuppressWarnings("unchecked")
            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Document uploaded to {}: {}", context, secureUrl);
            return secureUrl;
            
        } catch (IOException e) {
            log.error("Failed to upload document to {}: {}", context, e.getMessage());
            throw new RuntimeException("Document upload failed", e);
        }
    }

    @Override
    public String uploadQRCode(byte[] qrImageBytes, String employeeNumber) {
        try {
            String publicId = String.format("erp/hr/qrcodes/%s", employeeNumber);
            
            Map uploadResult = cloudinary.uploader().upload(qrImageBytes,
                Map.of(
                    "public_id", publicId,
                    "resource_type", "image",
                    "format", "png"
                ));

            @SuppressWarnings("unchecked")
            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("QR code uploaded for employee: {}", employeeNumber);
            return secureUrl;
            
        } catch (Exception e) {
            log.error("Failed to upload QR code for {}: {}", employeeNumber, e.getMessage());
            return null; // QR optional
        }
    }
}

