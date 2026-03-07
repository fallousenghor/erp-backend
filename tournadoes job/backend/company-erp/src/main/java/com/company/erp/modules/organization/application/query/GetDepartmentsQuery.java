package com.company.erp.modules.organization.application.query;

import org.springframework.data.domain.Pageable;

public record GetDepartmentsQuery(
        String name,
        String code,
        Boolean active,
        Pageable pageable
) {}
