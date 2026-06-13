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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Tag(
        name = "Payment Management",
        description = "APIs for creating and managing payment transactions"
)
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(
            summary = "Create Payment",
            description = "Creates a new payment transaction and publishes an event to Kafka"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Payment Created Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Request / Insufficient Balance"),
            @ApiResponse(responseCode = "409", description = "Duplicate Payment Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Get Transaction By ID",
            description = "Fetches a payment transaction using transaction ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction Found"),
            @ApiResponse(responseCode = "404", description = "Transaction Not Found")
    })
    @GetMapping("/payments/{transactionId}")
    public ResponseEntity<PaymentTransaction> getPaymentById(
            @PathVariable UUID transactionId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(transactionId)
        );
    }


    @Operation(
            summary = "Get All Transactions",
            description = "Returns all payment transactions"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions Retrieved Successfully")
    })
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentTransaction>> getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }
}