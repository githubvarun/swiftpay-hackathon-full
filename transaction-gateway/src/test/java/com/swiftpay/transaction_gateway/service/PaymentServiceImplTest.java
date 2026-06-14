package com.swiftpay.transaction_gateway.service;

import com.swiftpay.transaction_gateway.constants.TransactionConstants;
import com.swiftpay.transaction_gateway.dto.PaymentRequest;
import com.swiftpay.transaction_gateway.dto.PaymentResponse;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.exception.DuplicatePaymentException;
import com.swiftpay.transaction_gateway.exception.TransactionNotFoundException;
import com.swiftpay.transaction_gateway.producer.PaymentEventProducer;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentEventProducer producer;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest request;

    @BeforeEach
    void setUp() {

        request = PaymentRequest.builder()
                .senderId(1L)
                .receiverId(2L)
                .amount(BigDecimal.valueOf(100))
                .currency("INR")
                .build();
    }

    @Test
    void shouldCreatePaymentSuccessfully() {

        when(redisTemplate.hasKey(anyString()))
                .thenReturn(false);

        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);

        ArgumentCaptor<PaymentTransaction> captor =
                ArgumentCaptor.forClass(PaymentTransaction.class);

        PaymentResponse response =
                paymentService.createPayment(request);

        verify(redisTemplate)
                .hasKey("1-2-100");

        verify(valueOperations)
                .set(
                        eq("1-2-100"),
                        eq(TransactionConstants.PROCESSED),
                        eq(Duration.ofHours(24))
                );

        verify(paymentTransactionRepository)
                .save(captor.capture());

        verify(producer)
                .publish(any());

        assertNotNull(response);
        assertEquals("PENDING", response.getStatus());
        assertEquals(
                TransactionConstants.PAYMENT_ACCEPTED,
                response.getMessage()
        );
    }

    @Test
    void shouldThrowDuplicatePaymentException() {

        when(redisTemplate.hasKey(anyString()))
                .thenReturn(true);

        DuplicatePaymentException exception =
                assertThrows(
                        DuplicatePaymentException.class,
                        () -> paymentService.createPayment(request)
                );

        assertEquals(
                TransactionConstants.DUPLICATE_PAYMENT_MESSAGE,
                exception.getMessage()
        );

        verify(paymentTransactionRepository, never())
                .save(any());

        verify(producer, never())
                .publish(any());
    }

    @Test
    void shouldReturnPaymentById() {

        UUID transactionId = UUID.randomUUID();

        PaymentTransaction transaction =
                PaymentTransaction.builder()
                        .transactionId(transactionId)
                        .build();

        when(paymentTransactionRepository.findById(transactionId))
                .thenReturn(Optional.of(transaction));

        PaymentTransaction result =
                paymentService.getPaymentById(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.getTransactionId());
    }

    @Test
    void shouldThrowTransactionNotFoundException() {

        UUID transactionId = UUID.randomUUID();

        when(paymentTransactionRepository.findById(transactionId))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> paymentService.getPaymentById(transactionId)
        );
    }

    @Test
    void shouldReturnAllPayments() {

        List<PaymentTransaction> transactions =
                List.of(
                        PaymentTransaction.builder().build(),
                        PaymentTransaction.builder().build()
                );

        when(paymentTransactionRepository.findAll())
                .thenReturn(transactions);

        List<PaymentTransaction> result =
                paymentService.getAllPayments();

        assertEquals(2, result.size());

        verify(paymentTransactionRepository)
                .findAll();
    }
}