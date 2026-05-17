package com.example.devmatch.dto;

import com.example.devmatch.model.Role;

public record AuthResponse(
        String message,
        Long userId,
        String username,
        String email,
        Role role
) {
}