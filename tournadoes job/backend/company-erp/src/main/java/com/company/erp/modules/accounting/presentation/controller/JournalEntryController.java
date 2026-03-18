package com.company.erp.modules.accounting.presentation.controller;

import com.company.erp.modules.accounting.application.dto.request.CreateJournalEntryRequest;
import com.company.erp.modules.accounting.application.dto.response.JournalEntryResponse;
import com.company.erp.modules.accounting.application.service.JournalEntryService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounting/journal-entries")
@Tag(name = "Journal Entries", description = "Accounting — Journal entry management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class JournalEntryController {

    private final JournalEntryService service;

    @PostMapping
    @Operation(summary = "Create a new journal entry")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> create(@Valid @RequestBody CreateJournalEntryRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get journal entry by ID")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(service.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List journal entries with filtering")
    public ResponseEntity<ApiResponse<PageResponse<JournalEntryResponse>>> findAll(
            @RequestParam(required = false) String reference,
            @RequestParam(required = false) String accountCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ApiResponse.success(service.findAll(reference, accountCode, pageable)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update journal entry")
    public ResponseEntity<ApiResponse<JournalEntryResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody CreateJournalEntryRequest request) {
        return ResponseEntity.ok(ApiResponse.success(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete journal entry")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

