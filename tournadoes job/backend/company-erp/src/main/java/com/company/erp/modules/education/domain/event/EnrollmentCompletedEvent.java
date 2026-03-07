package com.company.erp.modules.education.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class EnrollmentCompletedEvent extends BaseDomainEvent {
    private final UUID enrollmentId;
    private final UUID studentId;
    private final BigDecimal finalAverage;
    private final boolean passed;

    public EnrollmentCompletedEvent(UUID enrollmentId, UUID studentId,
                                     BigDecimal finalAverage, boolean passed) {
        super("ENROLLMENT_COMPLETED");
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.finalAverage = finalAverage;
        this.passed = passed;
    }
}
