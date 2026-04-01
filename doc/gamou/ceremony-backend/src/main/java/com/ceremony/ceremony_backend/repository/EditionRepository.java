// repository/EditionRepository.java
package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceremony.ceremony_backend.entity.Edition;

import java.util.Optional;

public interface EditionRepository extends JpaRepository<Edition, Long> {
    Optional<Edition> findByYear(Integer year);
    Optional<Edition> findByIsActiveTrue();
    boolean existsByYear(Integer year);
}
