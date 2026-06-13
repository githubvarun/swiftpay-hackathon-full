package com.swiftpay.transaction_gateway.exception;

import com.swiftpay.transaction_gateway.constants.TransactionConstants;
import com.swiftpay.transaction_gateway.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatePaymentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePayment(
            DuplicatePaymentException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(409)
                                .error(TransactionConstants.CONFLICT)
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFound(
            TransactionNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(404)
                                .error(TransactionConstants.NOT_FOUND)
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        String message =
                ex.getBindingResult()
                        .getFieldError()
                        .getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(400)
                                .error(TransactionConstants.BAD_REQUEST)
                                .message(message)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .timestamp(LocalDateTime.now())
                                .status(500)
                                .error(TransactionConstants.INTERNAL_SERVER_ERROR)
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(
            InsufficientBalanceException.class
    )
    public ResponseEntity<ErrorResponse> handleInsufficientBalance(InsufficientBalanceException ex) {

        ErrorResponse response =
                ErrorResponse.builder()
                        .error("BAD_REQUEST")
                        .message(ex.getMessage())
                        .build();

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}