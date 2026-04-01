// repository/MediaRepository.java
package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceremony.ceremony_backend.entity.Media;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByEditionId(Long editionId);
    List<Media> findByType(String type);
}
