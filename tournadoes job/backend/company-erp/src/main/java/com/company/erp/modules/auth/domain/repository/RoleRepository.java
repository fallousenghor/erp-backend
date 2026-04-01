package com.company.erp.modules.auth.domain.repository;

import com.company.erp.modules.auth.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(UUID id);

    Optional<Role> findByName(String name);

    boolean existsByName(String name);
    
    List<Role> findAll();
    
    void delete(Role role);
}
