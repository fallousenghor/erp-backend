package com.company.erp.modules.inventory.infrastructure.persistence;

import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import com.company.erp.modules.inventory.domain.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AssetRepositoryImpl implements AssetRepository {

    private final AssetJpaRepository jpaRepository;

    @Override public Asset save(Asset a)                              { return jpaRepository.save(a); }
    @Override public boolean existsByAssetCode(String code)           { return jpaRepository.existsByAssetCode(code); }
    @Override public long countByStatus(AssetStatus s)                { return jpaRepository.countByStatus(s); }

    @Override
    public Optional<Asset> findById(UUID id) {
        return jpaRepository.findByIdWithAssignments(id);
    }

    @Override
    public Page<Asset> findAll(Specification<Asset> spec, Pageable pageable) {
        return jpaRepository.findAll(spec, pageable);
    }
}
