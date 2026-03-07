package com.company.erp.modules.education.application.command;

public record RecordGradeCommand(java.util.UUID enrollmentId, java.util.UUID moduleId, java.math.BigDecimal score) {}
