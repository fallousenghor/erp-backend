package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CeremonialYearResponse {
    private Long id;
    private Integer year;
    private String theme;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private BigDecimal initialBudget;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal totalContributions;
    private BigDecimal balance;
    private Long mediaCount;
    private Long eventCount;
    private Long transactionCount;
}
