package com.swiftpay.ledger_service.controller;

import com.swiftpay.ledger_service.dto.BalanceResponse;
import com.swiftpay.ledger_service.entity.Account;
import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.repository.AccountRepository;
import com.swiftpay.ledger_service.service.LedgerQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerControllerTest {

    @Mock
    private LedgerQueryService ledgerQueryService;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private LedgerController ledgerController;

    @Test
    void shouldReturnUserHistory() {

        Long userId = 1L;

        LedgerEntry entry1 = LedgerEntry.builder().build();
        LedgerEntry entry2 = LedgerEntry.builder().build();

        List<LedgerEntry> entries =
                List.of(entry1, entry2);

        when(ledgerQueryService.getUserHistory(userId))
                .thenReturn(entries);

        List<LedgerEntry> result =
                ledgerController.getUserHistory(userId);

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(ledgerQueryService)
                .getUserHistory(userId);
    }

    @Test
    void shouldReturnBalanceSuccessfully() {

        Long userId = 1L;

        Account account =
                Account.builder()
                        .userId(userId)
                        .balance(BigDecimal.valueOf(5000))
                        .build();

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.of(account));

        BalanceResponse response =
                ledgerController.getBalance(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertEquals(
                BigDecimal.valueOf(5000),
                response.getBalance()
        );

        verify(accountRepository)
                .findByUserId(userId);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {

        Long userId = 1L;

        when(accountRepository.findByUserId(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> ledgerController.getBalance(userId)
        );

        verify(accountRepository)
                .findByUserId(userId);
    }
}