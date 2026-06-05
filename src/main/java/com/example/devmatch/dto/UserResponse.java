package com.example.devmatch.dto;

import com.example.devmatch.model.Role;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role
) {
}