package com.company.erp.modules.auth.infrastructure.persistence;

import com.company.erp.modules.auth.domain.model.RefreshToken;
import com.company.erp.modules.auth.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true, t.revokedAt = :now WHERE t.user = :user AND t.revoked = false")
    void revokeAllByUser(User user, LocalDateTime now);

    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now")
    void deleteAllExpiredBefore(LocalDateTime now);

    boolean existsByToken(String token);
}
