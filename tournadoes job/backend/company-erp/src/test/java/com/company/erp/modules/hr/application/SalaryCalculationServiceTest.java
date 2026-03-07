package com.company.erp.modules.hr.application;

import com.company.erp.modules.hr.application.service.SalaryCalculationService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SalaryCalculationServiceTest {

    private final SalaryCalculationService service = new SalaryCalculationService();

    @Test
    void calculateNetSalary_subtractsTaxCorrectly() {
        BigDecimal gross   = BigDecimal.valueOf(1_000_000);
        BigDecimal taxRate = BigDecimal.valueOf(20); // 20%
        BigDecimal net     = service.calculateNetSalary(gross, taxRate);
        assertEquals(0, BigDecimal.valueOf(800_000).compareTo(net));
    }

    @Test
    void calculateNetSalary_withZeroTax_returnsGross() {
        BigDecimal gross = BigDecimal.valueOf(500_000);
        BigDecimal net   = service.calculateNetSalary(gross, BigDecimal.ZERO);
        assertEquals(0, gross.compareTo(net));
    }
}
