package com.company.erp.modules.documents.application.dto.request;

import com.company.erp.modules.documents.domain.model.Document.DocumentCategory;
import com.company.erp.modules.documents.domain.model.Document.DocumentStatus;
import com.company.erp.modules.documents.domain.model.Document.DocumentType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UpdateDocumentRequest(
    String name,
    String description,
    DocumentType type,
    DocumentCategory category,
    DocumentStatus status,
    Boolean signatureRequired,
    String fileUrl,
    UUID departmentId,
    UUID employeeId,
    List<String> signedBy,
    LocalDateTime expiresAt
) {}

