package com.company.erp.modules.finance.presentation.controller;

import com.company.erp.modules.finance.application.dto.request.RecordExpenseRequest;
import com.company.erp.modules.finance.application.dto.request.UpdateExpenseRequest;
import com.company.erp.modules.finance.application.dto.response.ExpenseResponse;
import com.company.erp.modules.finance.application.dto.response.ExpenseSummaryResponse;
import com.company.erp.modules.finance.application.service.ExpenseService;
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
@RequestMapping("/api/v1/expenses")
@Tag(name = "Expenses", description = "Finance — Expense management")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "Record a new expense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> record(
            @Valid @RequestBody RecordExpenseRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(expenseService.record(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ExpenseResponse>>> findAll(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                expenseService.findAll(category, status, 
                        PageRequest.of(page, size, Sort.by("expenseDate").descending()))));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an expense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExpenseRequest request) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an expense")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        expenseService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve an expense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> approve(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.approve(id)));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject an expense")
    public ResponseEntity<ApiResponse<ExpenseResponse>> reject(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(expenseService.reject(id)));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get expense summary")
    public ResponseEntity<ApiResponse<ExpenseSummaryResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success(expenseService.getSummary()));
    }
}
