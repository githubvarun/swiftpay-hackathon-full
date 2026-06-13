package com.swiftpay.transaction_gateway.exception;

public class TransactionNotFoundException
        extends RuntimeException {

    public TransactionNotFoundException(String message) {
        super(message);
    }
}