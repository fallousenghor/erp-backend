package com.company.erp.modules.auth.domain.repository;

import com.company.erp.modules.auth.domain.model.RefreshToken;
import com.company.erp.modules.auth.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    
    boolean existsByToken(String token);


    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(String token);

    void revokeAllByUser(User user);

    void deleteAllExpired();

    void deleteById(UUID id);
}
