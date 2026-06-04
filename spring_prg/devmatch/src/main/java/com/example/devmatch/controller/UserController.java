package com.example.devmatch.controller;

import com.example.devmatch.dto.RegisterUserRequest;
import com.example.devmatch.dto.UserResponse;
import com.example.devmatch.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints for user registration and user management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register user", description = "Creates a new user with CLIENT or FREELANCER role")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @Operation(summary = "Get all users", description = "Returns all registered users")
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}