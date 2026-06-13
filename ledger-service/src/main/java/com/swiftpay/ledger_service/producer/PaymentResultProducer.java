package com.swiftpay.ledger_service.producer;

import com.swiftpay.ledger_service.config.KafkaTopics;
import com.swiftpay.ledger_service.dto.PaymentResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentResultProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(PaymentResultEvent event) {

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_RESULT_TOPIC,
                event
        );
    }
}