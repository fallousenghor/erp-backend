package com.company.erp.modules.education.application.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/** Grade calculation utilities — weighted average logic is in Enrollment aggregate. */
@Service
public class GradeCalculationService {

    public BigDecimal weightedAverage(Map<Double, BigDecimal> coefficientToGrade) {
        double totalCoef = coefficientToGrade.keySet().stream().mapToDouble(Double::doubleValue).sum();
        if (totalCoef == 0) return BigDecimal.ZERO;
        double sum = coefficientToGrade.entrySet().stream()
                .mapToDouble(e -> e.getKey() * e.getValue().doubleValue()).sum();
        return BigDecimal.valueOf(sum / totalCoef).setScale(2, RoundingMode.HALF_UP);
    }
}
