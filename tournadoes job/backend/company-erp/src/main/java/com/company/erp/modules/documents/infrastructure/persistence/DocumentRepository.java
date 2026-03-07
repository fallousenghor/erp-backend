package com.company.erp.modules.documents.infrastructure.persistence;

import com.company.erp.modules.documents.domain.model.Document;
import com.company.erp.modules.documents.domain.model.Document.DocumentCategory;
import com.company.erp.modules.documents.domain.model.Document.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    Page<Document> findAll(Pageable pageable);

    Page<Document> findByCategory(DocumentCategory category, Pageable pageable);

    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);

    Page<Document> findByCategoryAndStatus(DocumentCategory category, DocumentStatus status, Pageable pageable);

    @Query("SELECT d FROM Document d WHERE " +
           "(:category IS NULL OR d.category = :category) AND " +
           "(:status IS NULL OR d.status = :status) AND " +
           "(:search IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Document> searchDocuments(
        @Param("category") DocumentCategory category,
        @Param("status") DocumentStatus status,
        @Param("search") String search,
        Pageable pageable
    );

    List<Document> findByEmployeeId(UUID employeeId);

    List<Document> findByDepartmentId(UUID departmentId);

    long countByStatus(DocumentStatus status);
}

