package com.swiftpay.transaction_gateway.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentResponse {

    private UUID transactionId;

    private String status;

    private String message;
}