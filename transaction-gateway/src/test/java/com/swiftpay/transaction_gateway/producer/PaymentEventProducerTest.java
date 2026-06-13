package com.swiftpay.transaction_gateway.producer;

import com.swiftpay.transaction_gateway.dto.PaymentEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventProducerTest {

    @Mock
    private KafkaTemplate<String,Object> kafkaTemplate;

    @InjectMocks
    private PaymentEventProducer producer;

    @Test
    void publish_shouldSendEvent() {

        PaymentEvent event =
                PaymentEvent.builder().build();

        producer.publish(event);

        verify(kafkaTemplate)
                .send(anyString(), eq(event));
    }
}