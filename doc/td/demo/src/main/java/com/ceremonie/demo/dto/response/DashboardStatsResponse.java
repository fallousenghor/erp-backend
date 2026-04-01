package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private Long totalMembers;
    private Long activeMembers;
    private Long totalUsers;
    private BigDecimal currentBalance;
    private BigDecimal totalIncomeThisYear;
    private BigDecimal totalExpenseThisYear;
    private Long upcomingEvents;
    private Long unpaidContributions;
    private Long activeMaterialLoans;
    private Long totalMedias;
    private String activeCeremonialYear;
}
