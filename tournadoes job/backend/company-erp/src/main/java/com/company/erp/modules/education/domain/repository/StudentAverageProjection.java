package com.company.erp.modules.education.domain.repository;

import java.math.BigDecimal;
import java.util.UUID;

public interface StudentAverageProjection {
    UUID getStudentId();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getProgramTitle();
    String getAvatarUrl();
    BigDecimal getAverage();
    Long getGradesCount();
}

