package com.swiftpay.transaction_gateway.service;

import com.swiftpay.transaction_gateway.dto.PaymentRequest;
import com.swiftpay.transaction_gateway.dto.PaymentResponse;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;

import java.util.List;
import java.util.UUID;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);
    PaymentTransaction getPaymentById(UUID transactionId);

    List<PaymentTransaction> getAllPayments();
}