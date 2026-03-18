package com.company.erp.modules.finance.infrastructure.persistence.specification;

import com.company.erp.modules.finance.domain.model.Expense;
import org.springframework.data.jpa.domain.Specification;

public class ExpenseSpecification {

    public static Specification<Expense> build(String category, String status) {
        Specification<Expense> spec = null;

        if (category != null && !category.isEmpty()) {
            spec = Specification.where(hasCategory(category));
        }

        if (status != null && !status.isEmpty()) {
            Specification<Expense> statusSpec = hasStatus(status);
            if (spec == null) {
                spec = statusSpec;
            } else {
                spec = spec.and(statusSpec);
            }
        }

        return spec;
    }

    private static Specification<Expense> hasCategory(String category) {
        return (root, query, cb) -> {
            try {
                Expense.ExpenseCategory cat = Expense.ExpenseCategory.valueOf(category.toUpperCase());
                return cb.equal(root.get("category"), cat);
            } catch (IllegalArgumentException e) {
                return cb.equal(root.get("category"), category);
            }
        };
    }

    private static Specification<Expense> hasStatus(String status) {
        return (root, query, cb) -> {
            try {
                Expense.ExpenseStatus stat = Expense.ExpenseStatus.valueOf(status.toUpperCase());
                return cb.equal(root.get("status"), stat);
            } catch (IllegalArgumentException e) {
                return cb.equal(root.get("status"), status);
            }
        };
    }
}

