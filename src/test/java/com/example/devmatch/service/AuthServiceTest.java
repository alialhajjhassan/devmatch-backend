package com.example.devmatch.service;

import com.example.devmatch.dto.AuthResponse;
import com.example.devmatch.dto.LoginRequest;
import com.example.devmatch.exception.InvalidCredentialsException;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_shouldReturnAuthResponse_whenCredentialsAreValid() {

        LoginRequest request = new LoginRequest(
                "client@test.com",
                "password123"
        );

        User user = new User();
        user.setId(1L);
        user.setUsername("client_test");
        user.setEmail("client@test.com");
        user.setPassword("hashed_password");
        user.setRole(Role.CLIENT);

        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed_password")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");


        AuthResponse response = authService.login(request);


        assertEquals("fake-jwt-token", response.token());
        assertEquals(1L, response.userId());
        assertEquals("client_test", response.username());
        assertEquals("client@test.com", response.email());
        assertEquals(Role.CLIENT, response.role());

        verify(userRepository).findByEmail("client@test.com");
        verify(passwordEncoder).matches("password123", "hashed_password");
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenEmailDoesNotExist() {

        LoginRequest request = new LoginRequest(
                "missing@test.com",
                "password123"
        );

        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());


        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(userRepository).findByEmail("missing@test.com");
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(jwtService);
    }

    @Test
    void login_shouldThrowInvalidCredentialsException_whenPasswordIsWrong() {

        LoginRequest request = new LoginRequest(
                "client@test.com",
                "wrongpassword"
        );

        User user = new User();
        user.setId(1L);
        user.setUsername("client_test");
        user.setEmail("client@test.com");
        user.setPassword("hashed_password");
        user.setRole(Role.CLIENT);

        when(userRepository.findByEmail("client@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed_password")).thenReturn(false);


        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(userRepository).findByEmail("client@test.com");
        verify(passwordEncoder).matches("wrongpassword", "hashed_password");
        verifyNoInteractions(jwtService);
    }
}