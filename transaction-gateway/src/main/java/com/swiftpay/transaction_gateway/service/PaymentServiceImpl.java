package com.swiftpay.transaction_gateway.service;

import com.swiftpay.transaction_gateway.constants.TransactionConstants;
import com.swiftpay.transaction_gateway.dto.PaymentEvent;
import com.swiftpay.transaction_gateway.dto.PaymentRequest;
import com.swiftpay.transaction_gateway.dto.PaymentResponse;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.entity.TransactionStatus;
import com.swiftpay.transaction_gateway.exception.DuplicatePaymentException;
import com.swiftpay.transaction_gateway.exception.InsufficientBalanceException;
import com.swiftpay.transaction_gateway.exception.TransactionNotFoundException;
import com.swiftpay.transaction_gateway.producer.PaymentEventProducer;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentEventProducer producer;
    private final StringRedisTemplate redisTemplate;
    private final PaymentTransactionRepository paymentTransactionRepository;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        String key =
                request.getSenderId() +
                        "-" +
                        request.getReceiverId() +
                        "-" +
                        request.getAmount();

        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {

            throw new DuplicatePaymentException(
                    TransactionConstants.DUPLICATE_PAYMENT_MESSAGE
            );
        }


        redisTemplate.opsForValue()
                .set(
                        key,
                        TransactionConstants.PROCESSED,
                        Duration.ofHours(24)
                );

        PaymentTransaction transaction =
                PaymentTransaction.builder()
                        .transactionId(UUID.randomUUID())
                        .senderId(request.getSenderId())
                        .receiverId(request.getReceiverId())
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .status(TransactionStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

        paymentTransactionRepository.save(transaction);

        PaymentEvent event =
                PaymentEvent.builder()
                        .transactionId(transaction.getTransactionId())
                        .senderId(transaction.getSenderId())
                        .receiverId(transaction.getReceiverId())
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .status(transaction.getStatus().name())
                        .createdAt(transaction.getCreatedAt())
                        .build();

        producer.publish(event);

        return PaymentResponse.builder()
                .transactionId(transaction.getTransactionId())
                .status(transaction.getStatus().name())
                .message(TransactionConstants.PAYMENT_ACCEPTED)
                .build();
    }

    @Override
    public PaymentTransaction getPaymentById(UUID transactionId) {

        return paymentTransactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException(
                                "Transaction not found : " + transactionId
                        ));
    }

    @Override
    public List<PaymentTransaction> getAllPayments() {

        return paymentTransactionRepository.findAll();
    }
}