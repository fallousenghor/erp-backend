package com.company.erp.modules.auth.application.command;

public record RegisterUserCommand(String username, String email, String password, String firstName, String lastName) {}
