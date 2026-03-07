package com.company.erp.modules.finance.infrastructure.persistence.specification;

import com.company.erp.modules.finance.domain.model.Invoice;
import com.company.erp.modules.finance.domain.model.valueobject.InvoiceStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InvoiceSpecification {

    private InvoiceSpecification() {}

    public static Specification<Invoice> build(String clientName, String status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (clientName != null && !clientName.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("clientName")),
                        "%" + clientName.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                try {
                    predicates.add(cb.equal(root.get("status"),
                            InvoiceStatus.valueOf(status.toUpperCase())));
                } catch (IllegalArgumentException ignored) {}
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
