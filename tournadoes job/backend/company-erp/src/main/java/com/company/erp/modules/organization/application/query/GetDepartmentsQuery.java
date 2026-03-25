package com.company.erp.modules.organization.application.query;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public record GetDepartmentsQuery(
        String name,
        String code,
        Boolean active,
        BigDecimal minBudget,
        BigDecimal maxBudget,
        Pageable pageable
) {}
