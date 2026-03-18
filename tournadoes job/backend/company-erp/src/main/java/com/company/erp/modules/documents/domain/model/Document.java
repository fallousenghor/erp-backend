package com.company.erp.modules.documents.domain.model;

import com.company.erp.shared.base.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverrides({
    @AttributeOverride(name = "id", column = @Column(name = "id", updatable = false, nullable = false)),
    @AttributeOverride(name = "createdAt", column = @Column(name = "created_at", nullable = false, updatable = false)),
    @AttributeOverride(name = "updatedAt", column = @Column(name = "updated_at", nullable = false)),
    @AttributeOverride(name = "version", column = @Column(name = "version", nullable = false)),
    @AttributeOverride(name = "createdBy", column = @Column(name = "created_by")),
    @AttributeOverride(name = "updatedBy", column = @Column(name = "updated_by"))
})
public class Document extends BaseAuditEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    @Column(nullable = false)
    private boolean signatureRequired;

    @Column(name = "file_url", length = 10000000)
    private String fileUrl;

    @Column(name = "uploaded_by")
    private String uploadedBy;

    @Column(name = "department_id")
    private java.util.UUID departmentId;

    @Column(name = "employee_id")
    private java.util.UUID employeeId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "document_signatures", joinColumns = @JoinColumn(name = "document_id"))
    @Column(name = "signed_by")
    @Builder.Default
    private List<String> signedBy = new ArrayList<>();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public enum DocumentType {
        CONTRACT,
        CNSS,
        ID,
        DIPLOMA,
        INVOICE,
        REPORT,
        POLICY,
        OTHER
    }

    public enum DocumentCategory {
        RH,
        FINANCE,
        JURIDIQUE,
        TECHNIQUE,
        COMMERCIAL,
        GENERAL
    }

    public enum DocumentStatus {
        DRAFT,
        PENDING_SIGNATURE,
        SIGNED,
        EXPIRED
    }
}
