package com.swiftpay.transaction_gateway.dto;

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
