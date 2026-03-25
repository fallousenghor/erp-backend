package com.company.erp.modules.organization.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Comprehensive department response with full details including head history and budget.
 */
public record DepartmentDetailResponse(
        UUID id,
        String name,
        String code,
        String description,
        boolean active,
        boolean deleted,
        LocalDateTime deletedAt,
        BudgetInfo budget,
        CurrentHeadInfo currentHead,
        List<HeadHistoryItem> headHistory,
        int employeeCount,
        int positionCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public record BudgetInfo(
            BigDecimal allocated,
            BigDecimal spent,
            BigDecimal remaining,
            String currency
    ) {}

    public record CurrentHeadInfo(
            UUID employeeId,
            String employeeName,
            LocalDate startDate
    ) {}

    public record HeadHistoryItem(
            UUID employeeId,
            String employeeName,
            LocalDate startDate,
            LocalDate endDate,
            boolean isCurrent
    ) {}
}
