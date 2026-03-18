package com.company.erp.modules.documents.application.dto.response;

import com.company.erp.modules.documents.domain.model.Document;
import com.company.erp.modules.documents.domain.model.Document.DocumentCategory;
import com.company.erp.modules.documents.domain.model.Document.DocumentStatus;
import com.company.erp.modules.documents.domain.model.Document.DocumentType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DocumentResponse(
    UUID id,
    String name,
    String description,
    DocumentType type,
    DocumentCategory category,
    DocumentStatus status,
    Long version,  // Changed from int to Long to match BaseEntity
    boolean signatureRequired,
    String fileUrl,
    String uploadedBy,
    LocalDateTime uploadedAt,
    LocalDateTime updatedAt,
    UUID departmentId,
    UUID employeeId,
    List<String> signedBy,
    LocalDateTime expiresAt
) {
    public static DocumentResponse fromEntity(Document doc) {
        return new DocumentResponse(
            doc.getId(),
            doc.getName(),
            doc.getDescription(),
            doc.getType(),
            doc.getCategory(),
            doc.getStatus(),
            doc.getVersion(),  // Version is primitive Long, never null
            doc.isSignatureRequired(),
            doc.getFileUrl(),
            doc.getUploadedBy(),
            doc.getCreatedAt(),
            doc.getUpdatedAt(),
            doc.getDepartmentId(),
            doc.getEmployeeId(),
(doc.getSignedBy() != null ? List.copyOf(doc.getSignedBy()) : List.of()),
            doc.getExpiresAt()
        );
    }
}

