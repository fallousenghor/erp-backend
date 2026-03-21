package com.company.erp.modules.auth.infrastructure.persistence;

import com.company.erp.modules.auth.domain.model.RefreshToken;
import com.company.erp.modules.auth.domain.model.User;
import com.company.erp.modules.auth.domain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

@Override
    public boolean existsByToken(String token) {
        return jpaRepository.existsByToken(token);
    }

    private final RefreshTokenJpaRepository jpaRepository;

    @Override
    public RefreshToken save(RefreshToken token) {
        return jpaRepository.save(token);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void revokeAllByUser(User user) {
        jpaRepository.revokeAllByUser(user, LocalDateTime.now());
    }

    @Override
    @Transactional
    public void deleteAllExpired() {
        jpaRepository.deleteAllExpiredBefore(LocalDateTime.now());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
