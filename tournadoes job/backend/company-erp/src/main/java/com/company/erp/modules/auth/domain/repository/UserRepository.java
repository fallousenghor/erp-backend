package com.company.erp.modules.auth.domain.repository;

import com.company.erp.modules.auth.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository interface for User aggregate.
 * Infrastructure layer provides the implementation.
 */
public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void deleteById(UUID id);
    Page<User> findAll(Pageable pageable);
}
