package com.swiftpay.transaction_gateway.exception;

import com.swiftpay.transaction_gateway.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void handleDuplicatePayment() {

        ResponseEntity<ErrorResponse> response =
                handler.handleDuplicatePayment(
                        new DuplicatePaymentException("duplicate")
                );

        assertEquals(409,
                response.getStatusCode().value());
    }

    @Test
    void handleTransactionNotFound() {

        ResponseEntity<ErrorResponse> response =
                handler.handleTransactionNotFound(
                        new TransactionNotFoundException("not found")
                );

        assertEquals(404,
                response.getStatusCode().value());
    }

    @Test
    void handleGeneric() {

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(
                        new RuntimeException("error")
                );

        assertEquals(500,
                response.getStatusCode().value());
    }
}