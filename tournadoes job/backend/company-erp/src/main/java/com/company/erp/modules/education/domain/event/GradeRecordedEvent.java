package com.company.erp.modules.education.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class GradeRecordedEvent extends BaseDomainEvent {
    private final UUID enrollmentId;
    private final UUID moduleId;
    private final UUID studentId;
    private final BigDecimal score;

    public GradeRecordedEvent(UUID enrollmentId, UUID moduleId, UUID studentId, BigDecimal score) {
        super("GRADE_RECORDED");
        this.enrollmentId = enrollmentId;
        this.moduleId = moduleId;
        this.studentId = studentId;
        this.score = score;
    }
}
