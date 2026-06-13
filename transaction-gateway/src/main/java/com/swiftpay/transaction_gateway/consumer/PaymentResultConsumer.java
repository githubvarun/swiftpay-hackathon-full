package com.swiftpay.transaction_gateway.consumer;

import com.swiftpay.transaction_gateway.dto.PaymentResultEvent;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.entity.TransactionStatus;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResultConsumer {

    private final PaymentTransactionRepository repository;

    @KafkaListener(
            topics = "payment-result-events",
            groupId = "transaction-group"
    )
    public void consume(PaymentResultEvent event) {

        log.info("Received Result Event : {}", event);

        PaymentTransaction transaction =
                repository.findById(event.getTransactionId())
                        .orElseThrow();

        transaction.setStatus(
                TransactionStatus.valueOf(event.getStatus())
        );

        repository.save(transaction);

        log.info(
                "Transaction Status Updated : {} -> {}",
                transaction.getTransactionId(),
                transaction.getStatus()
        );
    }


}