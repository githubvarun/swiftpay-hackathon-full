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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController controller;

    @Test
    void createPayment_shouldReturnResponse() {

        PaymentRequest request = new PaymentRequest();

        PaymentResponse response =
                PaymentResponse.builder()
                        .status("PENDING")
                        .build();

        when(paymentService.createPayment(request))
                .thenReturn(response);

        ResponseEntity<PaymentResponse> result =
                controller.createPayment(request);

        assertEquals(200, result.getStatusCode().value());
    }

    @Test
    void getPaymentById_shouldReturnTransaction() {

        UUID id = UUID.randomUUID();

        when(paymentService.getPaymentById(id))
                .thenReturn(
                        PaymentTransaction.builder()
                                .transactionId(id)
                                .build()
                );

        ResponseEntity<PaymentTransaction> result =
                controller.getPaymentById(id);

        assertEquals(id,
                result.getBody().getTransactionId());
    }

    @Test
    void getAllPayments_shouldReturnList() {

        when(paymentService.getAllPayments())
                .thenReturn(List.of(
                        PaymentTransaction.builder().build()
                ));

        ResponseEntity<List<PaymentTransaction>> result =
                controller.getAllPayments();

        assertEquals(1, result.getBody().size());
    }
}