package com.company.erp.security.userdetails;

import com.company.erp.modules.auth.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * Loads user details from the database for Spring Security authentication.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userJpaRepository.findByUsernameWithRolesAndPermissions(username)
                .map(user -> UserDetailsImpl.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .enabled(user.isEnabled())
                        .accountNonLocked(!user.isLocked())
                        .permissions(
                                user.getRoles().stream()
                                        .flatMap(role -> role.getPermissions().stream())
                                        .map(permission -> permission.getName())
                                        .collect(Collectors.toSet())
                        )
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username));
    }
}
