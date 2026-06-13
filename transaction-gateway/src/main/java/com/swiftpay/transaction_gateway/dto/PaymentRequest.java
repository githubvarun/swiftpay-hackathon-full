package com.swiftpay.transaction_gateway.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {

    @NotNull
    private Long senderId;

    @NotNull
    private Long receiverId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;
}