package com.ceremonie.demo.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ceremonie.demo.enums.ContributionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContributionResponse {
    private Long id;
    private Long memberId;
    private String memberName;
    private String memberNumber;
    private Long ceremonialYearId;
    private Integer ceremonialYear;
    private BigDecimal expectedAmount;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private ContributionStatus status;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private String notes;
}
