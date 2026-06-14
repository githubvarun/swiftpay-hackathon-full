package com.swiftpay.transaction_gateway.exception;

import com.swiftpay.transaction_gateway.constants.TransactionConstants;
import com.swiftpay.transaction_gateway.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldHandleDuplicatePaymentException() {

        DuplicatePaymentException ex =
                new DuplicatePaymentException("Duplicate payment");

        ResponseEntity<ErrorResponse> response =
                handler.handleDuplicatePayment(ex);

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Duplicate payment",
                response.getBody().getMessage());
        assertEquals(TransactionConstants.CONFLICT,
                response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleTransactionNotFoundException() {

        TransactionNotFoundException ex =
                new TransactionNotFoundException("Transaction not found");

        ResponseEntity<ErrorResponse> response =
                handler.handleTransactionNotFound(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("Transaction not found",
                response.getBody().getMessage());
        assertEquals(TransactionConstants.NOT_FOUND,
                response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleValidationException() {

        MethodArgumentNotValidException ex =
                mock(MethodArgumentNotValidException.class);

        BindingResult bindingResult =
                mock(BindingResult.class);

        FieldError fieldError =
                new FieldError(
                        "paymentRequest",
                        "amount",
                        "Amount is required"
                );

        when(ex.getBindingResult())
                .thenReturn(bindingResult);

        when(bindingResult.getFieldError())
                .thenReturn(fieldError);

        ResponseEntity<ErrorResponse> response =
                handler.handleValidation(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Amount is required",
                response.getBody().getMessage());
        assertEquals(TransactionConstants.BAD_REQUEST,
                response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleGenericException() {

        Exception ex =
                new Exception("Unexpected error");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(ex);

        assertEquals(500, response.getStatusCode().value());
        assertEquals("Unexpected error",
                response.getBody().getMessage());
        assertEquals(
                TransactionConstants.INTERNAL_SERVER_ERROR,
                response.getBody().getError()
        );
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void shouldHandleInsufficientBalanceException() {

        InsufficientBalanceException ex =
                new InsufficientBalanceException(
                        "Insufficient balance"
                );

        ResponseEntity<ErrorResponse> response =
                handler.handleInsufficientBalance(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Insufficient balance",
                response.getBody().getMessage());
        assertEquals("BAD_REQUEST",
                response.getBody().getError());
    }
}