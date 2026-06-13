package com.swiftpay.ledger_service.service;

import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.repository.LedgerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerQueryServiceTest {

    @Mock
    private LedgerRepository ledgerRepository;

    @InjectMocks
    private LedgerQueryService ledgerQueryService;

    @Test
    void getUserHistory_shouldReturnLedgerEntries() {

        LedgerEntry entry =
                LedgerEntry.builder()
                        .transactionId(UUID.randomUUID())
                        .senderId(1L)
                        .receiverId(2L)
                        .build();

        when(
                ledgerRepository.findBySenderIdOrReceiverId(
                        1L,
                        1L
                )
        ).thenReturn(List.of(entry));

        List<LedgerEntry> result =
                ledgerQueryService.getUserHistory(1L);

        assertEquals(1, result.size());

        verify(ledgerRepository)
                .findBySenderIdOrReceiverId(
                        1L,
                        1L
                );
    }

    @Test
    void getUserHistory_shouldReturnEmptyList() {

        when(
                ledgerRepository.findBySenderIdOrReceiverId(
                        1L,
                        1L
                )
        ).thenReturn(List.of());

        List<LedgerEntry> result =
                ledgerQueryService.getUserHistory(1L);

        assertTrue(result.isEmpty());

        verify(ledgerRepository)
                .findBySenderIdOrReceiverId(
                        1L,
                        1L
                );
    }
}