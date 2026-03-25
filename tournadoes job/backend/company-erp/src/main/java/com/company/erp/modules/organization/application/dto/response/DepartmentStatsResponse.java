package com.company.erp.modules.organization.application.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Department statistics and summary response.
 */
public record DepartmentStatsResponse(
        UUID id,
        String name,
        String code,
        int totalEmployees,
        int activeEmployees,
        int positionCount,
        int openPositions,
        BigDecimal budget,
        BigDecimal budgetUtilizationPercent,
        boolean hasActiveHead,
        String currentHeadName
) {}
