package com.company.erp.modules.documents.presentation.controller;

import com.company.erp.modules.documents.application.dto.request.CreateDocumentRequest;
import com.company.erp.modules.documents.application.dto.request.UpdateDocumentRequest;
import com.company.erp.modules.documents.application.dto.response.DocumentResponse;
import com.company.erp.modules.documents.application.service.DocumentService;
import com.company.erp.modules.documents.domain.model.Document.DocumentCategory;
import com.company.erp.modules.documents.domain.model.Document.DocumentStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @GetMapping
    public ResponseEntity<Page<DocumentResponse>> getAllDocuments(
        @RequestParam(required = false, defaultValue = "0") int page,
        @RequestParam(required = false, defaultValue = "10") int size,
        @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
        @RequestParam(required = false, defaultValue = "DESC") String sortDir,
        @RequestParam(required = false) DocumentCategory category,
        @RequestParam(required = false) DocumentStatus status,
        @RequestParam(required = false) String search
    ) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") 
            ? Sort.by(sortBy).ascending() 
            : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DocumentResponse> documents;
        if (search != null && !search.isEmpty()) {
            documents = documentService.searchDocuments(category, status, search, pageable);
        } else if (category != null && status != null) {
            // Filter by both
            documents = documentService.searchDocuments(category, status, null, pageable);
        } else if (category != null) {
            documents = documentService.getDocumentsByCategory(category, pageable);
        } else if (status != null) {
            documents = documentService.getDocumentsByStatus(status, pageable);
        } else {
            documents = documentService.getAllDocuments(pageable);
        }

        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocumentById(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(
        @Valid @RequestBody CreateDocumentRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(documentService.createDocument(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentResponse> updateDocument(
        @PathVariable UUID id,
        @RequestBody UpdateDocumentRequest request
    ) {
        return ResponseEntity.ok(documentService.updateDocument(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/signatures")
    public ResponseEntity<DocumentResponse> addSignature(
        @PathVariable UUID id,
        @RequestParam String signer
    ) {
        return ResponseEntity.ok(documentService.addSignature(id, signer));
    }

    @GetMapping("/stats/count")
    public ResponseEntity<Long> countByStatus(@RequestParam DocumentStatus status) {
        return ResponseEntity.ok(documentService.countByStatus(status));
    }
}

