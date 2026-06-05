package com.example.devmatch.service;

import com.example.devmatch.dto.RegisterUserRequest;
import com.example.devmatch.dto.UserResponse;
import com.example.devmatch.model.Role;
import com.example.devmatch.model.User;
import com.example.devmatch.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_shouldHashPasswordAndReturnUserResponse() {
        RegisterUserRequest request = new RegisterUserRequest(
                "client_test",
                "client@test.com",
                "password123",
                Role.CLIENT
        );

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("client_test");
        savedUser.setEmail("client@test.com");
        savedUser.setPassword("hashed_password");
        savedUser.setRole(Role.CLIENT);

        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);


        UserResponse response = userService.createUser(request);


        assertEquals(1L, response.id());
        assertEquals("client_test", response.username());
        assertEquals("client@test.com", response.email());
        assertEquals(Role.CLIENT, response.role());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }
}