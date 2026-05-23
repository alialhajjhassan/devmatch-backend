package com.example.devmatch.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateApplicationRequest(
        @NotBlank(message = "Cover letter is mandatory")
        String coverLetter
) {
}