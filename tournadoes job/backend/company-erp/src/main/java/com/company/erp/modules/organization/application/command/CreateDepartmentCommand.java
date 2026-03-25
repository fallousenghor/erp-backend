package com.company.erp.modules.organization.application.command;

import java.math.BigDecimal;

public record CreateDepartmentCommand(
        String name,
        String code,
        String description,
        BigDecimal budget
) {}
