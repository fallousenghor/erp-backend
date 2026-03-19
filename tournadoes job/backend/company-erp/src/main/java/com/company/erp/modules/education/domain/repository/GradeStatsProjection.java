package com.company.erp.modules.education.domain.repository;

import java.math.BigDecimal;

public interface GradeStatsProjection {
    BigDecimal getAverage();
    BigDecimal getHighest();
    BigDecimal getLowest();
    BigDecimal getPassRate();
    Long getTotalStudents();
}

