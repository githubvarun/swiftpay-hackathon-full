package com.swiftpay.transaction_gateway.consumer;

import com.swiftpay.transaction_gateway.dto.PaymentResultEvent;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.entity.TransactionStatus;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentResultConsumer {

    private final PaymentTransactionRepository repository;

    @RetryableTopic(
            attempts = "3",
            backoff = @Backoff(
                    delay = 5000,
                    multiplier = 2.0
            )
    )
    @KafkaListener(
            topics = "payment-result-events",
            groupId = "transaction-group"
    )
    public void consume(PaymentResultEvent event) {

        log.info(
                "Received payment result event for Transaction {} with status {}",
                event.getTransactionId(),
                event.getStatus()
        );

        PaymentTransaction transaction =
                repository.findById(
                        event.getTransactionId()
                ).orElseThrow();

        transaction.setStatus(
                TransactionStatus.valueOf(
                        event.getStatus()
                )
        );

        repository.save(transaction);

        log.info(
                "Transaction {} successfully updated to status {}",
                transaction.getTransactionId(),
                transaction.getStatus()
        );
    }

    @DltHandler
    public void dltHandler(
            PaymentResultEvent event
    ) {

        log.error(
                """
                
                TRANSACTION STATUS UPDATE FAILED
                
                Transaction Id : {}
                Final Status   : {}
                
                All retry attempts exhausted.
                Status update event moved to Dead Letter Topic.
                Transaction record could not be updated in Transaction Gateway.
                Manual investigation required.
                
                """,
                event.getTransactionId(),
                event.getStatus()
        );
    }


}