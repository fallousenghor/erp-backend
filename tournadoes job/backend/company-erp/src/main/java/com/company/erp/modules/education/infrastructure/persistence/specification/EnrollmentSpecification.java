package com.company.erp.modules.education.infrastructure.persistence.specification;

import com.company.erp.modules.education.domain.model.Enrollment;
import com.company.erp.modules.education.domain.model.valueobject.EnrollmentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnrollmentSpecification {

    private EnrollmentSpecification() {}

    public static Specification<Enrollment> build(UUID studentId, UUID programId, String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (studentId != null) {
                predicates.add(cb.equal(root.get("student").get("id"), studentId));
            }
            if (programId != null) {
                predicates.add(cb.equal(root.get("program").get("id"), programId));
            }
            if (status != null && !status.isBlank()) {
                try {
                    predicates.add(cb.equal(root.get("status"),
                            EnrollmentStatus.valueOf(status.toUpperCase())));
                } catch (IllegalArgumentException ignored) {}
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
