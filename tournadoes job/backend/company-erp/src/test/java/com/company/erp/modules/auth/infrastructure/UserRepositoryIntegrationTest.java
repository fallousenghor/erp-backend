package com.company.erp.modules.auth.infrastructure;

import com.company.erp.modules.auth.domain.model.User;
import com.company.erp.modules.auth.infrastructure.persistence.UserJpaRepository;
import com.company.erp.shared.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Test
    void adminUserShouldBeSeededByFlyway() {
        Optional<User> admin = userJpaRepository.findByUsername("admin");
        assertTrue(admin.isPresent(), "Default admin user should be seeded by V2 migration");
        assertEquals("admin@company.com", admin.get().getEmail());
        assertTrue(admin.get().isEnabled());
    }

    @Test
    void existsByUsername_returnsTrueForExistingUser() {
        assertTrue(userJpaRepository.existsByUsername("admin"));
        assertFalse(userJpaRepository.existsByUsername("nonexistent_user_xyz"));
    }
}
