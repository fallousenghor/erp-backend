package com.company.erp.modules.organization.application.command;

import java.time.LocalDate;
import java.util.UUID;

public record AssignDepartmentHeadCommand(
        UUID departmentId,
        UUID employeeId,
        String employeeName,
        LocalDate startDate
) {}
