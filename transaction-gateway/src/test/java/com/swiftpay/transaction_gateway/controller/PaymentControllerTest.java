package com.swiftpay.transaction_gateway.controller;

import com.swiftpay.transaction_gateway.dto.PaymentRequest;
import com.swiftpay.transaction_gateway.dto.PaymentResponse;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void shouldCreatePayment() {

        PaymentRequest request = PaymentRequest.builder()
                .senderId(1L)
                .receiverId(2L)
                .amount(BigDecimal.valueOf(100))
                .currency("INR")
                .build();

        PaymentResponse response = PaymentResponse.builder()
                .status("PENDING")
                .message("Payment Accepted")
                .build();

        when(paymentService.createPayment(request))
                .thenReturn(response);

        ResponseEntity<PaymentResponse> result =
                paymentController.createPayment(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals("PENDING", result.getBody().getStatus());

        verify(paymentService).createPayment(request);
    }

    @Test
    void shouldGetPaymentById() {

        UUID transactionId = UUID.randomUUID();

        PaymentTransaction transaction =
                PaymentTransaction.builder()
                        .transactionId(transactionId)
                        .build();

        when(paymentService.getPaymentById(transactionId))
                .thenReturn(transaction);

        ResponseEntity<PaymentTransaction> result =
                paymentController.getPaymentById(transactionId);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(transactionId,
                result.getBody().getTransactionId());

        verify(paymentService).getPaymentById(transactionId);
    }

    @Test
    void shouldGetAllPayments() {

        List<PaymentTransaction> transactions =
                List.of(
                        PaymentTransaction.builder().build(),
                        PaymentTransaction.builder().build()
                );

        when(paymentService.getAllPayments())
                .thenReturn(transactions);

        ResponseEntity<List<PaymentTransaction>> result =
                paymentController.getAllPayments();

        assertEquals(200, result.getStatusCode().value());
        assertEquals(2, result.getBody().size());

        verify(paymentService).getAllPayments();
    }
}