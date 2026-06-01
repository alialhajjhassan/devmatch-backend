package com.example.devmatch.dto;

import com.example.devmatch.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long applicationId,
        Long jobId,
        String jobTitle,
        Long clientId,
        String clientUsername,
        Long freelancerId,
        String freelancerUsername,
        BigDecimal amount,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}