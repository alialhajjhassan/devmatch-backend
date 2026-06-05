package com.example.devmatch.service;

import com.example.devmatch.exception.UnauthorizedActionException;
import com.example.devmatch.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new UnauthorizedActionException("User is not authenticated");
        }

        return user;
    }
}