package com.swiftpay.ledger_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceResponse {

    private Long userId;
    private BigDecimal balance;
}