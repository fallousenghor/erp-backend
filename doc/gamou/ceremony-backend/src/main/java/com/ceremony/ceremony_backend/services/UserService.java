package com.ceremony.ceremony_backend.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ceremony.ceremony_backend.dto.request.CreateTreasurerRequest;
import com.ceremony.ceremony_backend.entity.User;
import com.ceremony.ceremony_backend.enums.Role;
import com.ceremony.ceremony_backend.exception.BadRequestException;
import com.ceremony.ceremony_backend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordGeneratorService passwordGeneratorService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    @Transactional
    public User createTreasurer(CreateTreasurerRequest request) {
        // Vérifier si l'username existe déjà
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Ce nom d'utilisateur existe déjà");
        }
        
        // Générer un mot de passe fixe (non modifiable)
        String fixedPassword = passwordGeneratorService.generateFixedPassword();
        
        // Créer l'utilisateur trésorier
        User treasurer = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(fixedPassword))
                .role(Role.TREASURER)
                .isPasswordChangeable(false) // MOT DE PASSE NON MODIFIABLE
                .isTemporaryPassword(false)
                .enabled(true)
                .build();
        
        treasurer = userRepository.save(treasurer);
        
        // Envoyer l'email avec les identifiants
        try {
            emailService.sendTreasurerCredentialsEmail(
                request.getEmail(),
                request.getName(),
                request.getUsername(),
                fixedPassword // Mot de passe en clair dans l'email
            );
            log.info("Email d'identification trésorier envoyé à {}", request.getEmail());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email à {}", request.getEmail(), e);
        }
        
        return treasurer;
    }
    
    public List<User> getAllTreasurers() {
        return userRepository.findByRole(Role.TREASURER);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Utilisateur non trouvé"));
    }
    
    public void disableUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }
    
    public void enableUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(true);
        userRepository.save(user);
    }
}