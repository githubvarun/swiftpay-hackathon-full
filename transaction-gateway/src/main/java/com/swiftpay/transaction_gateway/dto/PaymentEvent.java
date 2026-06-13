package com.swiftpay.transaction_gateway.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent {

    private UUID transactionId;

    private Long senderId;

    private Long receiverId;

    private BigDecimal amount;

    private String currency;

    private String status;

    private LocalDateTime createdAt;
}