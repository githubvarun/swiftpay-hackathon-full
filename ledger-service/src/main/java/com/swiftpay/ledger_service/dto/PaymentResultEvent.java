package com.swiftpay.ledger_service.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResultEvent {

    private UUID transactionId;

    private String status;
}