package com.example.devmatch.dto;

import com.example.devmatch.model.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        String title,
        String description,
        BigDecimal budget,
        JobStatus status,
        LocalDateTime createdAt
) {
}