package com.swiftpay.ledger_service.controller;

import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.service.LedgerQueryService;
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
class LedgerControllerTest {

    @Mock
    private LedgerQueryService ledgerQueryService;

    @InjectMocks
    private LedgerController ledgerController;

    @Test
    void getUserHistory_shouldReturnEntries() {

        LedgerEntry entry =
                LedgerEntry.builder()
                        .transactionId(UUID.randomUUID())
                        .build();

        when(ledgerQueryService.getUserHistory(1L))
                .thenReturn(List.of(entry));

        List<LedgerEntry> result =
                ledgerController.getUserHistory(1L);

        assertEquals(1, result.size());

        verify(ledgerQueryService)
                .getUserHistory(1L);
    }

    @Test
    void getUserHistory_shouldReturnEmptyList() {

        when(ledgerQueryService.getUserHistory(1L))
                .thenReturn(List.of());

        List<LedgerEntry> result =
                ledgerController.getUserHistory(1L);

        assertTrue(result.isEmpty());
    }
}