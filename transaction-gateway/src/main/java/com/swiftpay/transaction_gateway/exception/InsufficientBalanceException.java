package com.swiftpay.transaction_gateway.exception;

public class InsufficientBalanceException
        extends RuntimeException {

    public InsufficientBalanceException(
            String message
    ) {
        super(message);
    }
}