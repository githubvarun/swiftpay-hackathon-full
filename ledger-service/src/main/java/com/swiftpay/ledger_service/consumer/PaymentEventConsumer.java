package com.swiftpay.ledger_service.consumer;

import com.swiftpay.ledger_service.config.KafkaTopics;
import com.swiftpay.ledger_service.dto.PaymentEvent;
import com.swiftpay.ledger_service.service.LedgerProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.kafka.annotation.DltHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final LedgerProcessingService ledgerProcessingService;


    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(
                    delay = 10000,
                    multiplier = 2.0
            )
    )
    @KafkaListener(
            topics = KafkaTopics.PAYMENT_TOPIC,
            groupId = "ledger-group"
    )
    public void consume(
            PaymentEvent event,
            @Header(
                    name = "kafka_receivedTopic",
                    required = false
            ) String topic) {

        log.info(
                "Received Event from topic : {}",
                topic
        );

        ledgerProcessingService.processPayment(event);
    }

    @DltHandler
    public void dltHandler(PaymentEvent event) {

        log.error(
                """
                PAYMENT PROCESSING FAILED
    
                Transaction Id : {}
                Sender Id      : {}
                Receiver Id    : {}
    
                All retry attempts exhausted.
                Message moved to Dead Letter Topic.
                Manual intervention required.
                """,
                event.getTransactionId(),
                event.getSenderId(),
                event.getReceiverId()
        );
    }

}