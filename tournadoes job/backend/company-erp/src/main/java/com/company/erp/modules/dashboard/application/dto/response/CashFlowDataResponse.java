package com.company.erp.modules.dashboard.application.dto.response;

import java.math.BigDecimal;

/**
 * DTO for cash flow data for charts
 */
public record CashFlowDataResponse(
        String month,
        BigDecimal incomes,
        BigDecimal expenses,
        BigDecimal balance
) {
    public static CashFlowDataResponse of(String month, BigDecimal incomes, BigDecimal expenses) {
        BigDecimal balance = incomes.subtract(expenses);
        return new CashFlowDataResponse(month, incomes, expenses, balance);
    }
}

