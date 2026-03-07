package com.company.erp.modules.organization.application.command;

import java.util.UUID;

public record UpdateDepartmentCommand(
        UUID id,
        String name,
        String description,
        Boolean active
) {}
