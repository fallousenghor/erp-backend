package com.ceremony.ceremony_backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ceremony.ceremony_backend.dto.request.ChangePasswordRequest;
import com.ceremony.ceremony_backend.dto.request.LoginRequest;
import com.ceremony.ceremony_backend.dto.response.LoginResponse;
import com.ceremony.ceremony_backend.entity.User;
import com.ceremony.ceremony_backend.exception.BadRequestException;
import com.ceremony.ceremony_backend.repository.UserRepository;
import com.ceremony.ceremony_backend.security.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        User user = (User) authentication.getPrincipal();
        
        return LoginResponse.builder()
                .token(jwt)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole().name())
                .isTemporaryPassword(user.getIsTemporaryPassword())
                .isPasswordChangeable(user.getIsPasswordChangeable())
                .build();
    }
    
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Utilisateur non trouvé"));
        
        // Vérifier si le mot de passe peut être changé
        if (!user.getIsPasswordChangeable()) {
            throw new BadRequestException("Votre mot de passe ne peut pas être modifié");
        }
        
        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Ancien mot de passe incorrect");
        }
        
        // Mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setIsTemporaryPassword(false);
        userRepository.save(user);
    }
    
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Utilisateur non authentifié");
        }
        return (User) authentication.getPrincipal();
    }
}