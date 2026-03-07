package com.company.erp.modules.inventory.infrastructure.persistence.specification;

import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AssetSpecification {

    private AssetSpecification() {}

    public static Specification<Asset> build(String name, String status, String category) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                try {
                    predicates.add(cb.equal(root.get("status"),
                            AssetStatus.valueOf(status.toUpperCase())));
                } catch (IllegalArgumentException ignored) {}
            }
            if (category != null && !category.isBlank()) {
                try {
                    predicates.add(cb.equal(root.get("category"),
                            Asset.AssetCategory.valueOf(category.toUpperCase())));
                } catch (IllegalArgumentException ignored) {}
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
