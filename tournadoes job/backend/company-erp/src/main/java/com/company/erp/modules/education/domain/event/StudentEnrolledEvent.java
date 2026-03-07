package com.company.erp.modules.education.domain.event;

import com.company.erp.shared.base.BaseDomainEvent;
import lombok.Getter;
import java.util.UUID;

@Getter
public class StudentEnrolledEvent extends BaseDomainEvent {
    private final UUID enrollmentId;
    private final UUID studentId;
    private final String studentName;
    private final UUID programId;
    private final String programTitle;

    public StudentEnrolledEvent(UUID enrollmentId, UUID studentId,
                                 String studentName, UUID programId, String programTitle) {
        super("STUDENT_ENROLLED");
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.programId = programId;
        this.programTitle = programTitle;
    }
}
