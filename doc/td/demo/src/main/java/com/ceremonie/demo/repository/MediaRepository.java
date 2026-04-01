package com.ceremonie.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ceremonie.demo.entity.CeremonialYear;
import com.ceremonie.demo.entity.Media;
import com.ceremonie.demo.enums.MediaType;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {
    
    List<Media> findByCeremonialYear(CeremonialYear ceremonialYear);
    
    List<Media> findByType(MediaType type);
    
    @Query("SELECT m FROM Media m WHERE m.ceremonialYear.id = :yearId AND m.deleted = false ORDER BY m.createdAt DESC")
    List<Media> findByCeremonialYearId(@Param("yearId") Long yearId);
    
    @Query("SELECT m FROM Media m WHERE " +
           "m.ceremonialYear.id = :yearId AND " +
           "m.type = :type AND " +
           "m.deleted = false ORDER BY m.createdAt DESC")
    List<Media> findByYearAndType(@Param("yearId") Long yearId, @Param("type") MediaType type);
    
    @Query("SELECT m FROM Media m WHERE m.ceremonialYear.active = true AND m.deleted = false ORDER BY m.createdAt DESC")
    List<Media> findByActiveCeremonialYear();
    
    @Query("SELECT m FROM Media m WHERE " +
           "(LOWER(m.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.tags) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "m.deleted = false")
    List<Media> searchMedias(@Param("search") String search);
    
    @Query("SELECT COUNT(m) FROM Media m WHERE " +
           "m.ceremonialYear.id = :yearId AND m.deleted = false")
    Long countMediasByYear(@Param("yearId") Long yearId);
    
    @Query("SELECT SUM(m.fileSize) FROM Media m WHERE " +
           "m.ceremonialYear.id = :yearId AND m.deleted = false")
    Long getTotalFileSizeByYear(@Param("yearId") Long yearId);
}