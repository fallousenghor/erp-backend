package com.company.erp.modules.organization.application.command;

public record CreateDepartmentCommand(
        String name,
        String code,
        String description
) {}
