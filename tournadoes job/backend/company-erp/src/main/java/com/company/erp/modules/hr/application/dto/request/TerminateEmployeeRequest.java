package com.company.erp.modules.hr.application.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Request to terminate an employee contract (soft delete).
 * Sets status to TERMINATED and termination_date.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TerminateEmployeeRequest {
    
    @NotNull(message = "Termination date is required")
    @FutureOrPresent(message = "Termination date cannot be in the past")
    private LocalDate terminationDate;
    
    private String reason;
    
    public LocalDate terminationDate() {
        return terminationDate;
    }
    
    public String reason() {
        return reason;
    }
}

