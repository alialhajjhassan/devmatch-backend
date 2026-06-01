package com.example.devmatch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreatePaymentRequest(
        @NotNull(message = "Amount is mandatory")
        @Positive(message = "Amount must be greater than zero")
        BigDecimal amount
) {
}