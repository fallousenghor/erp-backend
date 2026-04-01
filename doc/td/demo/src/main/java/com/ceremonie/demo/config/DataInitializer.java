package com.ceremonie.demo.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ceremonie.demo.entity.User;
import com.ceremonie.demo.enums.Role;
import com.ceremonie.demo.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Créer un utilisateur admin par défaut si aucun n'existe
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@ceremony.com")
                    .password(passwordEncoder.encode("admin123"))
                    .firstName("Admin")
                    .lastName("System")
                    .role(Role.ADMIN)
                    .active(true)
                    .build();
            
            userRepository.save(admin);
            log.info("✅ Utilisateur admin créé avec succès");
            log.info("   Username: admin");
            log.info("   Password: admin123");
        }
    }
}
