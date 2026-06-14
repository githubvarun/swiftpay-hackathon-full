package com.swiftpay.transaction_gateway.consumer;

import com.swiftpay.transaction_gateway.dto.PaymentResultEvent;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.entity.TransactionStatus;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentResultConsumerTest {

    @Mock
    private PaymentTransactionRepository repository;

    @InjectMocks
    private PaymentResultConsumer consumer;

    private PaymentResultEvent event;
    private PaymentTransaction transaction;

    @BeforeEach
    void setUp() {

        UUID transactionId = UUID.randomUUID();

        event = PaymentResultEvent.builder()
                .transactionId(transactionId)
                .status("COMPLETED")
                .build();

        transaction = PaymentTransaction.builder()
                .transactionId(transactionId)
                .status(TransactionStatus.PENDING)
                .build();
    }

    @Test
    void shouldUpdateTransactionStatusSuccessfully() {

        when(repository.findById(event.getTransactionId()))
                .thenReturn(Optional.of(transaction));

        consumer.consume(event);

        verify(repository).findById(event.getTransactionId());
        verify(repository).save(transaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {

        when(repository.findById(event.getTransactionId()))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> consumer.consume(event)
        );

        verify(repository).findById(event.getTransactionId());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldHandleDltEvent() {

        consumer.dltHandler(event);

        verifyNoInteractions(repository);
    }
}