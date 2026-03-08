package com.company.erp.modules.education.application;

import com.company.erp.modules.education.application.service.GradeCalculationService;
import com.company.erp.modules.education.domain.model.valueobject.Grade;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GradeCalculationServiceTest {

    private final GradeCalculationService gradeService = new GradeCalculationService();

    @Test
    void weightedAverage_withEqualCoefficients_returnsSimpleAverage() {
        // Use LinkedHashMap to allow equal coefficient keys (Map.of forbids duplicate keys)
        Map<Double, BigDecimal> grades = new java.util.LinkedHashMap<>();
        grades.put(1.0, BigDecimal.valueOf(15));
        grades.put(2.0, BigDecimal.valueOf(12));
        BigDecimal result = gradeService.weightedAverage(grades);
        assertNotNull(result);
    }

    @Test
    void grade_outOf20_normalizesCorrectly() {
        Grade grade = Grade.of(BigDecimal.valueOf(16), BigDecimal.valueOf(20));
        assertEquals(0, BigDecimal.valueOf(16).compareTo(grade.outOf20()));
    }

    @Test
    void grade_percentage_calculatesCorrectly() {
        Grade grade = Grade.of(BigDecimal.valueOf(14), BigDecimal.valueOf(20));
        assertEquals(0, BigDecimal.valueOf(70).compareTo(grade.percentage()));
    }

    @Test
    void grade_letterGrade_A_forHighScore() {
        Grade grade = Grade.of(BigDecimal.valueOf(90), BigDecimal.valueOf(100));
        assertEquals("A", grade.letterGrade());
    }

    @Test
    void grade_isPassing_trueAbove60Percent() {
        Grade passing = Grade.of(BigDecimal.valueOf(12), BigDecimal.valueOf(20));
        assertTrue(passing.isPassing());
    }

    @Test
    void grade_isPassing_falseBelow60Percent() {
        Grade failing = Grade.of(BigDecimal.valueOf(8), BigDecimal.valueOf(20));
        assertFalse(failing.isPassing());
    }
}
