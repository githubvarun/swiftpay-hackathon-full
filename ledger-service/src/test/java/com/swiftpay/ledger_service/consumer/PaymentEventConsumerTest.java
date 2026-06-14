package com.swiftpay.ledger_service.consumer;

import com.swiftpay.ledger_service.dto.PaymentEvent;
import com.swiftpay.ledger_service.service.LedgerProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventConsumerTest {

    @Mock
    private LedgerProcessingService ledgerProcessingService;

    @InjectMocks
    private PaymentEventConsumer paymentEventConsumer;

    @Test
    void shouldConsumePaymentEvent() {

        PaymentEvent event = PaymentEvent.builder()
                .transactionId(UUID.randomUUID())
                .senderId(1L)
                .receiverId(2L)
                .build();

        paymentEventConsumer.consume(
                event,
                "payment-topic"
        );

        verify(ledgerProcessingService)
                .processPayment(event);
    }

    @Test
    void shouldHandleDltMessage() {

        PaymentEvent event = PaymentEvent.builder()
                .transactionId(UUID.randomUUID())
                .senderId(1L)
                .receiverId(2L)
                .build();

        paymentEventConsumer.dltHandler(event);

        verifyNoInteractions(ledgerProcessingService);
    }
}