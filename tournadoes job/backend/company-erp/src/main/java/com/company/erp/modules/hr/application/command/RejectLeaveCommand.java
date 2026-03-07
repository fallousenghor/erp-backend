package com.company.erp.modules.hr.application.command;

public record RejectLeaveCommand(java.util.UUID leaveRequestId, String reason) {}
