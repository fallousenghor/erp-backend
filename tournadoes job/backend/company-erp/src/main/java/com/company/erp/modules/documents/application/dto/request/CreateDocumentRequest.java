package com.company.erp.modules.documents.application.dto.request;

import com.company.erp.modules.documents.domain.model.Document.DocumentCategory;
import com.company.erp.modules.documents.domain.model.Document.DocumentStatus;
import com.company.erp.modules.documents.domain.model.Document.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateDocumentRequest(
    @NotBlank(message = "Name is required")
    String name,
    String description,
    @NotNull(message = "Type is required")
    DocumentType type,
    @NotNull(message = "Category is required")
    DocumentCategory category,
    DocumentStatus status,
    boolean signatureRequired,
    String fileUrl,
    String uploadedBy,
    UUID departmentId,
    UUID employeeId,
    List<String> signedBy,
    LocalDateTime expiresAt
) {}

