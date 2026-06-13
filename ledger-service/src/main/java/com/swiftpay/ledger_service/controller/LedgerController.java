package com.swiftpay.ledger_service.controller;

import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.service.LedgerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@Tag(
        name = "Ledger Management",
        description = "APIs for querying ledger transactions and account history"
)
@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerQueryService ledgerQueryService;

    @Operation(
            summary = "Get User Ledger History",
            description = "Returns all ledger entries associated with a user"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ledger History Retrieved"),
            @ApiResponse(responseCode = "404", description = "User Not Found")
    })
    @GetMapping("/user/{userId}")
    public List<LedgerEntry> getUserHistory(
            @PathVariable Long userId) {

        return ledgerQueryService.getUserHistory(userId);
    }
}