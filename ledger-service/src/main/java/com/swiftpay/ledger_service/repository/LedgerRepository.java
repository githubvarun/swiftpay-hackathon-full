package com.swiftpay.ledger_service.repository;

import com.swiftpay.ledger_service.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerRepository
        extends JpaRepository<LedgerEntry, UUID> {

    List<LedgerEntry> findBySenderIdOrReceiverId(
            Long senderId,
            Long receiverId
    );
}