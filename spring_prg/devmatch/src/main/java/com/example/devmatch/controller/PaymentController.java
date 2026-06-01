package com.example.devmatch.controller;

import com.example.devmatch.dto.CreatePaymentRequest;
import com.example.devmatch.dto.PaymentResponse;
import com.example.devmatch.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications/{applicationId}/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @PathVariable Long applicationId,
            @Valid @RequestBody CreatePaymentRequest request
    ) {
        PaymentResponse response = paymentService.createPayment(applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}