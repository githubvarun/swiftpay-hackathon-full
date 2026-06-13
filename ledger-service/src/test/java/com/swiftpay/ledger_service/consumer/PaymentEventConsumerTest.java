package com.swiftpay.ledger_service.consumer;

import com.swiftpay.ledger_service.dto.PaymentEvent;
import com.swiftpay.ledger_service.service.LedgerProcessingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentEventConsumerTest {

    @Mock
    private LedgerProcessingService ledgerProcessingService;

    @InjectMocks
    private PaymentEventConsumer consumer;

    @Test
    void consume_shouldCallLedgerProcessingService() {

        PaymentEvent event =
                PaymentEvent.builder().build();

        consumer.consume(event);

        verify(ledgerProcessingService)
                .processPayment(event);
    }
}