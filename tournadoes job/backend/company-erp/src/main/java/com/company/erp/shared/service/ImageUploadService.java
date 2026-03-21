package com.company.erp.shared.service;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.lang.Nullable;

public interface ImageUploadService {
    String uploadImage(MultipartFile image, String context);
    String uploadDocument(MultipartFile document, String context);
    @Nullable String uploadQRCode(byte[] qrImageBytes, String employeeNumber);
}

