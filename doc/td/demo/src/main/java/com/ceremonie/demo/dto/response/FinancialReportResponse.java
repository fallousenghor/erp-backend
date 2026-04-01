package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialReportResponse {
    private Long ceremonialYearId;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalContributions;
    private BigDecimal totalDonations;
    private BigDecimal balance;
    private BigDecimal initialBudget;
    private Long transactionCount;
    private Long contributionCount;
    private Long paidContributionCount;
    private Long unpaidContributionCount;
    private Map<String, BigDecimal> expensesByCategory;
    private List<TransactionResponse> recentTransactions;
}
