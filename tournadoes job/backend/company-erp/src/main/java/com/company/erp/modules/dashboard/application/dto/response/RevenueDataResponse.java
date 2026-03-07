package com.company.erp.modules.dashboard.application.dto.response;

import java.math.BigDecimal;

/**
 * DTO for monthly revenue data for charts
 */
public record RevenueDataResponse(
        String month,
        BigDecimal revenus,
        BigDecimal depenses,
        BigDecimal benefice
) {
    public static RevenueDataResponse of(String month, BigDecimal revenus, BigDecimal depenses) {
        BigDecimal benefice = revenus.subtract(depenses);
        return new RevenueDataResponse(month, revenus, depenses, benefice);
    }
}

