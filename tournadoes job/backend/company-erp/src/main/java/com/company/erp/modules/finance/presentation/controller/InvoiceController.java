package com.company.erp.modules.finance.presentation.controller;

import com.company.erp.modules.finance.application.dto.request.CreateInvoiceRequest;
import com.company.erp.modules.finance.application.dto.request.ProcessPaymentRequest;
import com.company.erp.modules.finance.application.dto.request.UpdateInvoiceRequest;
import com.company.erp.modules.finance.application.dto.response.FinancialSummaryResponse;
import com.company.erp.modules.finance.application.dto.response.InvoiceResponse;
import com.company.erp.modules.finance.application.service.InvoiceService;
import com.company.erp.shared.response.ApiResponse;
import com.company.erp.shared.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@Tag(name = "Invoices", description = "Finance — Invoice management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create a new invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> create(
            @Valid @RequestBody CreateInvoiceRequest request) {
        return ResponseEntity.status(201)
                .body(ApiResponse.created(invoiceService.create(request)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<ApiResponse<InvoiceResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.findById(id)));
    }

    @GetMapping
    @Operation(summary = "List invoices with filtering")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> findAll(
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                invoiceService.findAll(clientName, status,
                        PageRequest.of(page, size, Sort.by("issueDate").descending()))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateInvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an invoice")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/send")
    @Operation(summary = "Send invoice to client")
    public ResponseEntity<ApiResponse<InvoiceResponse>> send(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.send(id)));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.cancel(id)));
    }

    @PostMapping("/{id}/payments")
    @Operation(summary = "Process a payment for an invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> processPayment(
            @PathVariable UUID id,
            @Valid @RequestBody ProcessPaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                invoiceService.processPayment(id, request)));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get financial summary")
    public ResponseEntity<ApiResponse<FinancialSummaryResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getFinancialSummary()));
    }
}
