package com.company.erp.shared.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository for audit log persistence.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    Page<AuditLog> findByUsername(String username, Pageable pageable);

    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId, Pageable pageable);

    List<AuditLog> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);

    Page<AuditLog> findByAction(String action, Pageable pageable);

    // Additional methods for audit statistics and filtering
    long countByCreatedAtAfter(LocalDateTime timestamp);

    long countByStatus(String status);
    
    Page<AuditLog> findByModule(String module, Pageable pageable);
}
