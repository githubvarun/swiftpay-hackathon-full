package com.swiftpay.ledger_service.controller;

import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.service.LedgerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerQueryService ledgerQueryService;

    @GetMapping("/user/{userId}")
    public List<LedgerEntry> getUserHistory(
            @PathVariable Long userId) {

        return ledgerQueryService.getUserHistory(userId);
    }
}