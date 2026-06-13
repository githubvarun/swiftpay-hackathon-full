package com.swiftpay.transaction_gateway.controller;

import com.swiftpay.transaction_gateway.dto.PaymentRequest;
import com.swiftpay.transaction_gateway.dto.PaymentResponse;
import com.swiftpay.transaction_gateway.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payments/{transactionId}")
    public ResponseEntity<PaymentTransaction> getPaymentById(
            @PathVariable UUID transactionId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(transactionId)
        );
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentTransaction>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }
}