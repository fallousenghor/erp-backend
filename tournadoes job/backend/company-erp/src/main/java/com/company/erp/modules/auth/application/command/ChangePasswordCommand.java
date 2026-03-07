package com.company.erp.modules.auth.application.command;

public record ChangePasswordCommand(java.util.UUID userId, String oldPassword, String newPassword) {}
