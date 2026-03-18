package com.company.erp.modules.finance.application.dto.response;

import java.math.BigDecimal;

public record ExpenseSummaryResponse(
        BigDecimal totalPending,
        BigDecimal totalApproved,
        BigDecimal totalPaid,
        BigDecimal totalRejected,
        BigDecimal totalAmount,
        long pendingCount,
        long approvedCount,
        long paidCount,
        long rejectedCount,
        long totalCount
) {}

