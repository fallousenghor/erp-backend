package com.company.erp.modules.inventory.infrastructure.persistence;

import com.company.erp.modules.inventory.domain.model.Asset;
import com.company.erp.modules.inventory.domain.model.valueobject.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetJpaRepository
        extends JpaRepository<Asset, UUID>, JpaSpecificationExecutor<Asset> {

    boolean existsByAssetCode(String assetCode);
    long countByStatus(AssetStatus status);

    @Query("""
            SELECT a FROM Asset a
            LEFT JOIN FETCH a.assignments ass
            WHERE a.id = :id
            """)
    Optional<Asset> findByIdWithAssignments(@Param("id") UUID id);
}
