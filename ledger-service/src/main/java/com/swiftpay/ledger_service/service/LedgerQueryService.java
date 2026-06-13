package com.swiftpay.ledger_service.service;

import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerQueryService {

    private final LedgerRepository ledgerRepository;

    public List<LedgerEntry> getUserHistory(Long userId) {

        return ledgerRepository.findBySenderIdOrReceiverId(
                userId,
                userId
        );
    }
}