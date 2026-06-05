package com.example.devmatch.controller;

import com.example.devmatch.dto.CreatePaymentRequest;
import com.example.devmatch.dto.PaymentResponse;
import com.example.devmatch.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/applications/{applicationId}/payments")
@Tag(name = "Payments", description = "Endpoints for simulated payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "Create simulated payment", description = "Creates a simulated payment for an ACCEPTED application")
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable Long applicationId,
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        PaymentResponse response = paymentService.createPayment(applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}