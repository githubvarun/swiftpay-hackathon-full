package com.swiftpay.transaction_gateway.exception;

public class DuplicatePaymentException
        extends RuntimeException {

    public DuplicatePaymentException(String message) {
        super(message);
    }
}