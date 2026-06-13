package com.swiftpay.transaction_gateway.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    @Test
    void duplicatePaymentException() {

        DuplicatePaymentException ex =
                new DuplicatePaymentException(
                        "duplicate"
                );

        assertEquals(
                "duplicate",
                ex.getMessage()
        );
    }

    @Test
    void transactionNotFoundException() {

        TransactionNotFoundException ex =
                new TransactionNotFoundException(
                        "not found"
                );

        assertEquals(
                "not found",
                ex.getMessage()
        );
    }

    @Test
    void insufficientBalanceException() {

        InsufficientBalanceException ex =
                new InsufficientBalanceException(
                        "balance"
                );

        assertEquals(
                "balance",
                ex.getMessage()
        );
    }
}