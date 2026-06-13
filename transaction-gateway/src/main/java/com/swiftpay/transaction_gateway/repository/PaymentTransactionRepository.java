package com.swiftpay.transaction_gateway.repository;

import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, UUID> {
}