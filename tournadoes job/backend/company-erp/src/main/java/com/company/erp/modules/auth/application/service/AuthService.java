package com.company.erp.modules.auth.application.service;

import com.company.erp.modules.auth.application.dto.request.LoginRequest;
import com.company.erp.modules.auth.application.dto.request.RefreshTokenRequest;
import com.company.erp.modules.auth.application.dto.request.RegisterRequest;
import com.company.erp.modules.auth.application.dto.response.AuthResponse;
import com.company.erp.modules.auth.application.dto.response.TokenResponse;
import com.company.erp.modules.auth.domain.event.UserCreatedEvent;
import com.company.erp.modules.auth.domain.event.UserLockedEvent;
import com.company.erp.modules.auth.domain.event.UserLoggedInEvent;
import com.company.erp.modules.auth.domain.model.RefreshToken;
import com.company.erp.modules.auth.domain.model.Role;
import com.company.erp.modules.auth.domain.model.User;
import com.company.erp.modules.auth.domain.repository.RefreshTokenRepository;
import com.company.erp.modules.auth.domain.repository.RoleRepository;
import com.company.erp.modules.auth.domain.repository.UserRepository;
import com.company.erp.security.jwt.JwtProperties;
import com.company.erp.security.jwt.JwtTokenProvider;
import com.company.erp.security.userdetails.UserDetailsImpl;
import com.company.erp.shared.audit.Auditable;
import com.company.erp.shared.event.DomainEventPublisher;
import com.company.erp.shared.exception.BusinessException;
import com.company.erp.shared.exception.ErrorCode;
import com.company.erp.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Authentication service — handles login, register, token refresh, logout.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final DomainEventPublisher eventPublisher;

    // ── Login ────────────────────────────────────────────────────────────────

    @Auditable(action = "USER_LOGIN", entity = "User")
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_CREDENTIALS));

        if (user.isLocked()) {
            throw new LockedException(ErrorCode.ACCOUNT_LOCKED.getMessage());
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );

            user.recordLogin();
            userRepository.save(user);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
            String refreshToken = generateAndSaveRefreshToken(user, userDetails);

            eventPublisher.publish(new UserLoggedInEvent(user.getId(), user.getUsername(), ""));

            return buildAuthResponse(user, userDetails, accessToken, refreshToken);

        } catch (BadCredentialsException ex) {
            handleFailedAttempt(user);
            throw new BusinessException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    // ── Register ─────────────────────────────────────────────────────────────

    @Auditable(action = "USER_REGISTER", entity = "User")
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS,
                    "Username already taken: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.USER_ALREADY_EXISTS,
                    "Email already registered: " + request.email());
        }

        String roleName = request.roleName() != null ? request.roleName() : "ROLE_USER";
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.ROLE_NOT_FOUND, roleName));

        User user = User.builder()
                .username(request.username())
                .email(request.email().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .enabled(true)
                .locked(false)
                .build();

        user.addRole(role);
        user = userRepository.save(user);

        eventPublisher.publish(new UserCreatedEvent(user.getId(), user.getUsername(), user.getEmail()));

        UserDetailsImpl userDetails = buildUserDetails(user);
        String accessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String refreshToken = generateAndSaveRefreshToken(user, userDetails);

        return buildAuthResponse(user, userDetails, accessToken, refreshToken);
    }

    // ── Refresh ──────────────────────────────────────────────────────────────

    @Auditable(action = "TOKEN_REFRESH", entity = "RefreshToken")
    public TokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(request.refreshToken())
                .orElseThrow(() -> new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if (!storedToken.isValid()) {
            storedToken.revoke();
            refreshTokenRepository.save(storedToken);
            throw new BusinessException(storedToken.isExpired()
                    ? ErrorCode.REFRESH_TOKEN_EXPIRED
                    : ErrorCode.TOKEN_INVALID);
        }

        User user = storedToken.getUser();
        storedToken.revoke();
        refreshTokenRepository.save(storedToken);

        UserDetailsImpl userDetails = buildUserDetails(user);
        String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
        String newRefreshToken = generateAndSaveRefreshToken(user, userDetails);

        return new TokenResponse(newAccessToken, newRefreshToken,
                jwtProperties.getAccessTokenExpiration());
    }

    // ── Logout ───────────────────────────────────────────────────────────────

    @Auditable(action = "USER_LOGOUT", entity = "User")
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.revoke();
                    refreshTokenRepository.save(token);
                });
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void handleFailedAttempt(User user) {
        user.incrementFailedAttempts();
        if (user.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            user.lock();
            eventPublisher.publish(new UserLockedEvent(
                    user.getId(), user.getUsername(), user.getFailedAttempts()));
            log.warn("Account locked for user [{}] after {} failed attempts",
                    user.getUsername(), user.getFailedAttempts());
        }
        userRepository.save(user);
    }

    private String generateAndSaveRefreshToken(User user, UserDetailsImpl userDetails) {
        String rawToken;
        RefreshToken existingToken;
        
        do {
            rawToken = jwtTokenProvider.generateRefreshToken(userDetails);
            existingToken = refreshTokenRepository.findByToken(rawToken).orElse(null);
            if (existingToken != null) {
                // Rare collision - revoke existing token
                log.warn("Refresh token collision detected for user {}. Revoking existing token.", user.getUsername());
                existingToken.revoke();
                refreshTokenRepository.save(existingToken);
            }
        } while (existingToken != null); // Retry until unique token generated
        
        RefreshToken refreshToken = RefreshToken.builder()
                .token(rawToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(
                        jwtProperties.getRefreshTokenExpiration() / 1000))
                .build();
        refreshTokenRepository.save(refreshToken);
        return rawToken;
    }

    private AuthResponse buildAuthResponse(User user, UserDetailsImpl userDetails,
                                            String accessToken, String refreshToken) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                userDetails.getPermissions(),
                accessToken,
                refreshToken,
                jwtProperties.getAccessTokenExpiration()
        );
    }

    private UserDetailsImpl buildUserDetails(User user) {
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .enabled(user.isEnabled())
                .accountNonLocked(!user.isLocked())
                .permissions(
                        user.getRoles().stream()
                                .flatMap(r -> r.getPermissions().stream())
                                .map(p -> p.getName())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
