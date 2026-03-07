package com.company.erp.modules.auth.application;

import com.company.erp.modules.auth.application.dto.request.RegisterRequest;
import com.company.erp.modules.auth.application.service.AuthService;
import com.company.erp.modules.auth.domain.repository.UserRepository;
import com.company.erp.shared.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AuthService authService;

    @Test
    void register_whenUsernameAlreadyExists_throwsBusinessException() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        RegisterRequest request = new RegisterRequest(
                "existingUser", "test@example.com", "Password1!",
                "John", "Doe", null);

        assertThrows(BusinessException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void register_whenEmailAlreadyExists_throwsBusinessException() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        RegisterRequest request = new RegisterRequest(
                "newUser", "existing@example.com", "Password1!",
                "John", "Doe", null);

        assertThrows(BusinessException.class, () -> authService.register(request));
    }
}
