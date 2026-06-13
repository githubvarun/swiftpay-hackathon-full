package com.swiftpay.transaction_gateway.constants;

public final class TransactionConstants {

    private TransactionConstants() {
    }

    public static final String PAYMENT_ACCEPTED = "Payment Accepted";

    public static final String DUPLICATE_PAYMENT_MESSAGE =
            "Duplicate payment request within 24 hours";

    public static final String PROCESSED = "PROCESSED";

    public static final String CONFLICT = "CONFLICT";
    public static final String NOT_FOUND = "NOT_FOUND";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String INTERNAL_SERVER_ERROR =
            "INTERNAL_SERVER_ERROR";
    public static final String INSUFFICIENT_BALANCE =
            "Insufficient balance";
}