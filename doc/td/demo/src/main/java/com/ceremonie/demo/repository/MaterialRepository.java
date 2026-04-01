package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.Material;
import com.ceremonie.demo.enums.MaterialStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    
    Optional<Material> findByReferenceNumber(String referenceNumber);
    
    List<Material> findByStatus(MaterialStatus status);
    
    Boolean existsByReferenceNumber(String referenceNumber);
    
    @Query("SELECT m FROM Material m WHERE m.deleted = false ORDER BY m.name ASC")
    List<Material> findAllActive();
    
    @Query("SELECT m FROM Material m WHERE " +
           "(LOWER(m.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "m.referenceNumber LIKE CONCAT('%', :search, '%')) AND " +
           "m.deleted = false")
    List<Material> searchMaterials(@Param("search") String search);
    
    @Query("SELECT m FROM Material m WHERE " +
           "m.availableQuantity < :threshold AND m.deleted = false")
    List<Material> findLowStock(@Param("threshold") Integer threshold);
    
    @Query("SELECT m FROM Material m WHERE " +
           "m.availableQuantity = 0 AND m.deleted = false")
    List<Material> findOutOfStock();
    
    @Query("SELECT COUNT(m) FROM Material m WHERE m.deleted = false")
    Long countActiveMaterials();
    
    @Query("SELECT SUM(m.quantity) FROM Material m WHERE m.deleted = false")
    Long getTotalQuantity();
}