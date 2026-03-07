package com.company.erp.modules.inventory.domain.repository;

import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface AssetRepository {
    Asset save(Asset asset);
    Optional<Asset> findById(UUID id);
    boolean existsByAssetCode(String code);
    Page<Asset> findAll(Specification<Asset> spec, Pageable pageable);
    long countByStatus(AssetStatus status);
}
