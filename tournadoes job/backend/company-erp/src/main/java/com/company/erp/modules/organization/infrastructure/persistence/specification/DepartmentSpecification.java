package com.company.erp.modules.organization.infrastructure.persistence.specification;

import com.company.erp.modules.organization.domain.model.Department;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic filter specification for Department queries.
 */
public class DepartmentSpecification {

    private DepartmentSpecification() {}

    public static Specification<Department> build(String name, String code, Boolean active) {
        return build(name, code, active, null, null);
    }

    public static Specification<Department> build(String name, String code, Boolean active, 
                                                  BigDecimal minBudget, BigDecimal maxBudget) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Always exclude soft-deleted
            predicates.add(cb.isFalse(root.get("deleted")));

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }
            if (code != null && !code.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("code")),
                        "%" + code.toLowerCase() + "%"));
            }
            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }
            
            // Budget range filtering
            if (minBudget != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("budget"), minBudget));
            }
            if (maxBudget != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("budget"), maxBudget));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
