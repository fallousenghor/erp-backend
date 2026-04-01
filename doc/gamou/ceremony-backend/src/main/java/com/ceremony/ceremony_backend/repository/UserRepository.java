package com.ceremony.ceremony_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ceremony.ceremony_backend.entity.User;
import com.ceremony.ceremony_backend.enums.Role;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    List<User> findByRole(Role role);
}
