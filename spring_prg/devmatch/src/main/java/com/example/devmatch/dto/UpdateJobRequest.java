package com.example.devmatch.dto;

import com.example.devmatch.model.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateJobRequest(
        @NotBlank(message = "Title is mandatory")
        String title,

        @NotBlank(message = "Description is mandatory")
        String description,

        @NotNull(message = "Budget is mandatory")
        @Positive(message = "Budget must be greater than zero")
        BigDecimal budget,

        @NotNull(message = "Status is mandatory")
        JobStatus status
) {
}