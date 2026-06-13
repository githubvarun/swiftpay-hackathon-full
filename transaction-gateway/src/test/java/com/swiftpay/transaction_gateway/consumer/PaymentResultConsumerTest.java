package com.swiftpay.transaction_gateway.consumer;

import com.swiftpay.transaction_gateway.dto.PaymentResultEvent;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentResultConsumerTest {

    @Mock
    private PaymentTransactionRepository repository;

    @InjectMocks
    private PaymentResultConsumer consumer;

    @Test
    void consume_shouldUpdateTransactionStatus() {

        UUID id = UUID.randomUUID();

        PaymentResultEvent event =
                PaymentResultEvent.builder()
                        .transactionId(id)
                        .status("COMPLETED")
                        .build();

        PaymentTransaction transaction =
                PaymentTransaction.builder()
                        .transactionId(id)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(transaction));

        consumer.consume(event);

        verify(repository).save(transaction);
    }
}