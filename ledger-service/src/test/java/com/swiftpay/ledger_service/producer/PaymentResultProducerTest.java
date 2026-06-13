package com.swiftpay.ledger_service.producer;

import com.swiftpay.ledger_service.dto.PaymentResultEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentResultProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PaymentResultProducer producer;

    @Test
    void publish_shouldSendEventToKafka() {

        PaymentResultEvent event =
                PaymentResultEvent.builder()
                        .transactionId(UUID.randomUUID())
                        .status("COMPLETED")
                        .build();

        producer.publish(event);

        verify(kafkaTemplate)
                .send(anyString(), eq(event));
    }
}