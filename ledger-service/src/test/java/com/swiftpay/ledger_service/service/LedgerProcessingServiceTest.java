package com.swiftpay.ledger_service.service;

import com.swiftpay.ledger_service.dto.PaymentEvent;
import com.swiftpay.ledger_service.dto.PaymentResultEvent;
import com.swiftpay.ledger_service.entity.Account;
import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.entity.LedgerStatus;
import com.swiftpay.ledger_service.producer.PaymentResultProducer;
import com.swiftpay.ledger_service.repository.AccountRepository;
import com.swiftpay.ledger_service.repository.LedgerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerProcessingServiceTest {

    @Mock
    private PaymentResultProducer paymentResultProducer;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private LedgerRepository ledgerRepository;

    @InjectMocks
    private LedgerProcessingService ledgerProcessingService;

    @Test
    void processPayment_shouldCompletePayment() {

        UUID transactionId = UUID.randomUUID();

        PaymentEvent event =
                PaymentEvent.builder()
                        .transactionId(transactionId)
                        .senderId(1L)
                        .receiverId(2L)
                        .amount(BigDecimal.valueOf(500))
                        .currency("INR")
                        .createdAt(LocalDateTime.now())
                        .build();

        Account sender =
                Account.builder()
                        .id(1L)
                        .userId(1L)
                        .balance(BigDecimal.valueOf(1000))
                        .build();

        Account receiver =
                Account.builder()
                        .id(2L)
                        .userId(2L)
                        .balance(BigDecimal.valueOf(500))
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByUserId(2L))
                .thenReturn(Optional.of(receiver));

        ledgerProcessingService.processPayment(event);

        assertEquals(
                BigDecimal.valueOf(500),
                sender.getBalance()
        );

        assertEquals(
                BigDecimal.valueOf(1000),
                receiver.getBalance()
        );

        verify(accountRepository).save(sender);
        verify(accountRepository).save(receiver);

        verify(ledgerRepository).save(any(LedgerEntry.class));

        verify(paymentResultProducer)
                .publish(any(PaymentResultEvent.class));
    }

    @Test
    void processPayment_shouldSaveCompletedLedgerEntry() {

        UUID transactionId = UUID.randomUUID();

        PaymentEvent event =
                PaymentEvent.builder()
                        .transactionId(transactionId)
                        .senderId(1L)
                        .receiverId(2L)
                        .amount(BigDecimal.valueOf(200))
                        .currency("INR")
                        .createdAt(LocalDateTime.now())
                        .build();

        Account sender =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(1000))
                        .build();

        Account receiver =
                Account.builder()
                        .userId(2L)
                        .balance(BigDecimal.valueOf(500))
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByUserId(2L))
                .thenReturn(Optional.of(receiver));

        ledgerProcessingService.processPayment(event);

        ArgumentCaptor<LedgerEntry> captor =
                ArgumentCaptor.forClass(LedgerEntry.class);

        verify(ledgerRepository).save(captor.capture());

        LedgerEntry entry = captor.getValue();

        assertEquals(
                LedgerStatus.COMPLETED,
                entry.getStatus()
        );
    }

    @Test
    void processPayment_shouldPublishCompletedEvent() {

        UUID transactionId = UUID.randomUUID();

        PaymentEvent event =
                PaymentEvent.builder()
                        .transactionId(transactionId)
                        .senderId(1L)
                        .receiverId(2L)
                        .amount(BigDecimal.valueOf(100))
                        .currency("INR")
                        .createdAt(LocalDateTime.now())
                        .build();

        Account sender =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(1000))
                        .build();

        Account receiver =
                Account.builder()
                        .userId(2L)
                        .balance(BigDecimal.valueOf(100))
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByUserId(2L))
                .thenReturn(Optional.of(receiver));

        ledgerProcessingService.processPayment(event);

        ArgumentCaptor<PaymentResultEvent> captor =
                ArgumentCaptor.forClass(PaymentResultEvent.class);

        verify(paymentResultProducer)
                .publish(captor.capture());

        assertEquals(
                "COMPLETED",
                captor.getValue().getStatus()
        );
    }

    @Test
    void processPayment_shouldFailWhenInsufficientBalance() {

        UUID transactionId = UUID.randomUUID();

        PaymentEvent event =
                PaymentEvent.builder()
                        .transactionId(transactionId)
                        .senderId(1L)
                        .receiverId(2L)
                        .amount(BigDecimal.valueOf(5000))
                        .currency("INR")
                        .createdAt(LocalDateTime.now())
                        .build();

        Account sender =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(100))
                        .build();

        Account receiver =
                Account.builder()
                        .userId(2L)
                        .balance(BigDecimal.valueOf(500))
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByUserId(2L))
                .thenReturn(Optional.of(receiver));

        ledgerProcessingService.processPayment(event);

        verify(accountRepository, never()).save(any());

        verify(paymentResultProducer)
                .publish(any(PaymentResultEvent.class));
    }

    @Test
    void processPayment_shouldSaveFailedLedgerEntry() {

        UUID transactionId = UUID.randomUUID();

        PaymentEvent event =
                PaymentEvent.builder()
                        .transactionId(transactionId)
                        .senderId(1L)
                        .receiverId(2L)
                        .amount(BigDecimal.valueOf(5000))
                        .currency("INR")
                        .createdAt(LocalDateTime.now())
                        .build();

        Account sender =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(100))
                        .build();

        Account receiver =
                Account.builder()
                        .userId(2L)
                        .balance(BigDecimal.valueOf(500))
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByUserId(2L))
                .thenReturn(Optional.of(receiver));

        ledgerProcessingService.processPayment(event);

        ArgumentCaptor<LedgerEntry> captor =
                ArgumentCaptor.forClass(LedgerEntry.class);

        verify(ledgerRepository).save(captor.capture());

        assertEquals(
                LedgerStatus.FAILED,
                captor.getValue().getStatus()
        );
    }

    @Test
    void processPayment_shouldThrowWhenSenderMissing() {

        PaymentEvent event =
                PaymentEvent.builder()
                        .senderId(1L)
                        .receiverId(2L)
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> ledgerProcessingService.processPayment(event)
        );
    }

    @Test
    void processPayment_shouldThrowWhenReceiverMissing() {

        PaymentEvent event =
                PaymentEvent.builder()
                        .senderId(1L)
                        .receiverId(2L)
                        .build();

        Account sender =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(1000))
                        .build();

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(sender));

        when(accountRepository.findByUserId(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> ledgerProcessingService.processPayment(event)
        );
    }
}