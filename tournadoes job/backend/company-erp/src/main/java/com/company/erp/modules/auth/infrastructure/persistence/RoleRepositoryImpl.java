package com.company.erp.modules.auth.infrastructure.persistence;

import com.company.erp.modules.auth.domain.model.Role;
import com.company.erp.modules.auth.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;

    @Override public Role save(Role r)                       { return jpaRepository.save(r); }
    @Override public Optional<Role> findById(UUID id)        { return jpaRepository.findById(id); }
    @Override public Optional<Role> findByName(String name)  { return jpaRepository.findByName(name); }
    @Override public boolean existsByName(String name)       { return jpaRepository.existsByName(name); }
    @Override public List<Role> findAll()                    { return jpaRepository.findAll(); }
    @Override public void delete(Role r)                     { jpaRepository.delete(r); }
}
