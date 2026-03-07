package com.company.erp.modules.hr.application.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/** Salary calculation utilities — net salary, bonuses, deductions. */
@Service
public class SalaryCalculationService {

    public BigDecimal calculateNetSalary(BigDecimal gross, BigDecimal taxRate) {
        BigDecimal tax = gross.multiply(taxRate).divide(BigDecimal.valueOf(100));
        return gross.subtract(tax);
    }
}
