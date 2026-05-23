package com.example.devmatch.dto;

import com.example.devmatch.model.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationResponse(
        Long id,
        Long jobId,
        String jobTitle,
        Long freelancerId,
        String freelancerUsername,
        String coverLetter,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
}