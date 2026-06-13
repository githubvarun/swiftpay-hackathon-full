package com.swiftpay.transaction_gateway.producer;

import com.swiftpay.transaction_gateway.config.KafkaTopics;
import com.swiftpay.transaction_gateway.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(PaymentEvent event) {

        kafkaTemplate.send(
                KafkaTopics.PAYMENT_TOPIC,
                event
        );
    }
}