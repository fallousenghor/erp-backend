package com.company.erp.modules.documents.application.service;

import com.company.erp.modules.documents.application.dto.request.CreateDocumentRequest;
import com.company.erp.modules.documents.application.dto.request.UpdateDocumentRequest;
import com.company.erp.modules.documents.application.dto.response.DocumentResponse;
import com.company.erp.modules.documents.domain.model.Document;
import com.company.erp.modules.documents.domain.model.Document.DocumentCategory;
import com.company.erp.modules.documents.domain.model.Document.DocumentStatus;
import com.company.erp.modules.documents.infrastructure.persistence.DocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Page<DocumentResponse> getAllDocuments(Pageable pageable) {
        return documentRepository.findAll(pageable).map(DocumentResponse::fromEntity);
    }

    public Page<DocumentResponse> getDocumentsByCategory(DocumentCategory category, Pageable pageable) {
        return documentRepository.findByCategory(category, pageable).map(DocumentResponse::fromEntity);
    }

    public Page<DocumentResponse> getDocumentsByStatus(DocumentStatus status, Pageable pageable) {
        return documentRepository.findByStatus(status, pageable).map(DocumentResponse::fromEntity);
    }

    public Page<DocumentResponse> searchDocuments(DocumentCategory category, DocumentStatus status, String search, Pageable pageable) {
        return documentRepository.searchDocuments(category, status, search, pageable).map(DocumentResponse::fromEntity);
    }

    public DocumentResponse getDocumentById(UUID id) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found: " + id));
        return DocumentResponse.fromEntity(document);
    }

    public DocumentResponse createDocument(CreateDocumentRequest request) {
        Document document = Document.builder()
            .name(request.name())
            .description(request.description())
            .type(request.type())
            .category(request.category())
            .status(request.status() != null ? request.status() : Document.DocumentStatus.DRAFT)
            .signatureRequired(request.signatureRequired())
            .fileUrl(request.fileUrl())
            .uploadedBy(request.uploadedBy())
            .departmentId(request.departmentId())
            .employeeId(request.employeeId())
            .signedBy(request.signedBy() != null ? request.signedBy() : new java.util.ArrayList<>())
            .expiresAt(request.expiresAt())
            .build();

        Document saved = documentRepository.save(document);
        log.info("Document created: {}", saved.getId());
        return DocumentResponse.fromEntity(saved);
    }

    public DocumentResponse updateDocument(UUID id, UpdateDocumentRequest request) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found: " + id));

        if (request.name() != null) {
            document.setName(request.name());
        }
        if (request.description() != null) {
            document.setDescription(request.description());
        }
        if (request.type() != null) {
            document.setType(request.type());
        }
        if (request.category() != null) {
            document.setCategory(request.category());
        }
        if (request.status() != null) {
            document.setStatus(request.status());
        }
        if (request.signatureRequired() != null) {
            document.setSignatureRequired(request.signatureRequired());
        }
        if (request.fileUrl() != null) {
            document.setFileUrl(request.fileUrl());
        }
        if (request.departmentId() != null) {
            document.setDepartmentId(request.departmentId());
        }
        if (request.employeeId() != null) {
            document.setEmployeeId(request.employeeId());
        }
        if (request.signedBy() != null) {
            document.setSignedBy(request.signedBy());
        }
        if (request.expiresAt() != null) {
            document.setExpiresAt(request.expiresAt());
        }

        Document saved = documentRepository.save(document);
        log.info("Document updated: {}", saved.getId());
        return DocumentResponse.fromEntity(saved);
    }

    public void deleteDocument(UUID id) {
        documentRepository.deleteById(id);
        log.info("Document deleted: {}", id);
    }

    public DocumentResponse addSignature(UUID id, String signer) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document not found: " + id));

        List<String> signedBy = new java.util.ArrayList<>(document.getSignedBy());
        if (!signedBy.contains(signer)) {
            signedBy.add(signer);
            document.setSignedBy(signedBy);
            
            // Auto-update status if all required signatures are collected
            if (document.isSignatureRequired() && !signedBy.isEmpty()) {
                document.setStatus(Document.DocumentStatus.SIGNED);
            }
            
            documentRepository.save(document);
        }
        
        return DocumentResponse.fromEntity(document);
    }

    public long countByStatus(DocumentStatus status) {
        return documentRepository.countByStatus(status);
    }
}

