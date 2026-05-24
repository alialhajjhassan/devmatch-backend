package com.example.devmatch.dto;

import com.example.devmatch.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateApplicationStatusRequest(
        @NotNull(message = "Status is mandatory")
        ApplicationStatus status
) {
}