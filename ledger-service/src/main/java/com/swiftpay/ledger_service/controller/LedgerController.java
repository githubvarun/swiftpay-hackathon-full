package com.swiftpay.ledger_service.controller;

import com.swiftpay.ledger_service.dto.BalanceResponse;
import com.swiftpay.ledger_service.entity.Account;
import com.swiftpay.ledger_service.entity.LedgerEntry;
import com.swiftpay.ledger_service.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

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

    @Operation(
            summary = "Get Account Balance",
            description = "Returns the current balance available for a given user account"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Balance Retrieved Successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account Not Found"
            )
    })
    @GetMapping("/accounts/{userId}/balance")
    public BalanceResponse getBalance(
            @PathVariable Long userId) {

        Account account =
                accountRepository.findByUserId(userId)
                        .orElseThrow();

        return new BalanceResponse(
                userId,
                account.getBalance()
        );
    }
}