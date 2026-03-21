package com.company.erp.shared.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QRCodeService {

    private final ImageUploadService imageUploadService;

    @Value("${cloudinary.upload-folder:erp-uploads}")
    private String uploadFolder;

    private static final int QR_CODE_SIZE = 300;

    /**
     * Generate QR code containing employee information and upload to Cloudinary.
     * 
     * @param employeeNumber The employee number (matricule)
     * @param firstName Employee first name
     * @param lastName Employee last name
     * @param email Employee email
     * @return The URL of the uploaded QR code image
     */
    public String generateAndUploadQRCode(String employeeNumber, String firstName, 
                                          String lastName, String email) {
        try {
            // Create QR code content with employee info
            String qrContent = String.format(
                "MATRICULE: %s\nNOM: %s\nPRENOM: %s\nEMAIL: %s",
                employeeNumber, lastName, firstName, email
            );

            // Generate QR code image as byte array
            byte[] qrCodeImage = generateQRCodeImage(qrContent);

            // Upload to Cloudinary
            String qrCodeUrl = imageUploadService.uploadQRCode(
                qrCodeImage, 
                employeeNumber
            );

            log.info("QR code generated and uploaded for employee: {}", employeeNumber);
            return qrCodeUrl;

        } catch (Exception e) {
            log.error("Failed to generate QR code for employee: {}", employeeNumber, e);
            return null;
        }
    }

    /**
     * Generate QR code image as byte array.
     * 
     * @param content The content to encode in the QR code
     * @return The QR code image as byte array
     * @throws WriterException If encoding fails
     * @throws IOException If image writing fails
     */
    public byte[] generateQRCodeImage(String content) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 
                                                   QR_CODE_SIZE, QR_CODE_SIZE, hints);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Generate QR code as Base64 string (for direct display).
     * 
     * @param content The content to encode
     * @return Base64 encoded PNG image
     */
    public String generateQRCodeAsBase64(String content) {
        try {
            byte[] qrCodeImage = generateQRCodeImage(content);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrCodeImage);
        } catch (Exception e) {
            log.error("Failed to generate QR code as Base64", e);
            return null;
        }
    }
}

