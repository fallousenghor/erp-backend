package com.company.erp.modules.finance.application.dto.response;

import java.math.BigDecimal;

public record FinancialSummaryResponse(
        BigDecimal totalRevenue,
        BigDecimal totalPaid,
        BigDecimal totalPending,
        BigDecimal totalExpenses,
        BigDecimal netBalance,
        long invoiceCount,
        long paidInvoiceCount,
        long pendingInvoiceCount,
        long expenseCount,
        String currency
) {}
