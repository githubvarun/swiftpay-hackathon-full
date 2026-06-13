package com.swiftpay.transaction_gateway.service;

import com.swiftpay.transaction_gateway.dto.PaymentRequest;
import com.swiftpay.transaction_gateway.dto.PaymentResponse;
import com.swiftpay.transaction_gateway.entity.Account;
import com.swiftpay.transaction_gateway.entity.PaymentTransaction;
import com.swiftpay.transaction_gateway.exception.DuplicatePaymentException;
import com.swiftpay.transaction_gateway.exception.InsufficientBalanceException;
import com.swiftpay.transaction_gateway.exception.TransactionNotFoundException;
import com.swiftpay.transaction_gateway.producer.PaymentEventProducer;
import com.swiftpay.transaction_gateway.repository.AccountRepository;
import com.swiftpay.transaction_gateway.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentEventProducer producer;

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private PaymentTransactionRepository repository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private PaymentServiceImpl service;

    @Test
    void createPayment_shouldSucceed() {

        PaymentRequest request = new PaymentRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("INR");

        Account account =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(10000))
                        .build();

        when(redisTemplate.hasKey(anyString()))
                .thenReturn(false);

        when(redisTemplate.opsForValue())
                .thenReturn(valueOperations);

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(account));

        PaymentResponse response =
                service.createPayment(request);

        assertEquals("PENDING", response.getStatus());

        verify(repository).save(any());
    }

    @Test
    void createPayment_shouldThrowDuplicatePaymentException() {

        PaymentRequest request = new PaymentRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setAmount(BigDecimal.valueOf(100));
        request.setCurrency("INR");

        when(redisTemplate.hasKey(anyString()))
                .thenReturn(true);

        assertThrows(
                DuplicatePaymentException.class,
                () -> service.createPayment(request)
        );
    }

    @Test
    void createPayment_shouldThrowInsufficientBalanceException() {

        PaymentRequest request = new PaymentRequest();
        request.setSenderId(1L);
        request.setReceiverId(2L);
        request.setAmount(BigDecimal.valueOf(5000));
        request.setCurrency("INR");

        Account account =
                Account.builder()
                        .userId(1L)
                        .balance(BigDecimal.valueOf(100))
                        .build();

        when(redisTemplate.hasKey(anyString()))
                .thenReturn(false);

        when(accountRepository.findByUserId(1L))
                .thenReturn(Optional.of(account));

        assertThrows(
                InsufficientBalanceException.class,
                () -> service.createPayment(request)
        );
    }

    @Test
    void getPaymentById_shouldReturnTransaction() {

        UUID id = UUID.randomUUID();

        PaymentTransaction transaction =
                PaymentTransaction.builder()
                        .transactionId(id)
                        .build();

        when(repository.findById(id))
                .thenReturn(Optional.of(transaction));

        PaymentTransaction result =
                service.getPaymentById(id);

        assertEquals(id, result.getTransactionId());
    }

    @Test
    void getPaymentById_shouldThrowException() {

        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(
                TransactionNotFoundException.class,
                () -> service.getPaymentById(id)
        );
    }

    @Test
    void getAllPayments_shouldReturnList() {

        when(repository.findAll())
                .thenReturn(List.of(
                        PaymentTransaction.builder().build()
                ));

        List<PaymentTransaction> result =
                service.getAllPayments();

        assertEquals(1, result.size());
    }
}