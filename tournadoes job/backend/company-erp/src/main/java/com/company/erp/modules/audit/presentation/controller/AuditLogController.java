package com.company.erp.modules.audit.presentation.controller;

import com.company.erp.shared.audit.AuditLog;
import com.company.erp.shared.audit.AuditLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Audit Log Controller - System audit trail
 */
@RestController
@RequestMapping("/api/v1/audit-logs")
@Tag(name = "Audit Logs", description = "System audit trail and activity logs")
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    @GetMapping
    @Operation(summary = "List all audit logs with pagination and filters")
    public ResponseEntity<Page<AuditLog>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String status) {
        
        Page<AuditLog> logs = auditLogRepository.findAll(
            PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"))
        );
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID")
    public ResponseEntity<AuditLog> getAuditLog(@PathVariable UUID id) {
        return auditLogRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stats")
    @Operation(summary = "Get audit statistics")
    public ResponseEntity<Map<String, Long>> getAuditStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalLogs", auditLogRepository.count());

        LocalDateTime today = LocalDateTime.now().toLocalDate().atStartOfDay();
        stats.put("todayLogs", auditLogRepository.countByCreatedAtAfter(today));
        stats.put("errors", auditLogRepository.countByStatus("error"));
        stats.put("warnings", auditLogRepository.countByStatus("warning"));

        return ResponseEntity.ok(stats);
    }
}
