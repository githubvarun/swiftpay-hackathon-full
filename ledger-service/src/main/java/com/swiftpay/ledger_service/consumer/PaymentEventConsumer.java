package com.swiftpay.ledger_service.consumer;

import com.swiftpay.ledger_service.dto.PaymentEvent;
import com.swiftpay.ledger_service.service.LedgerProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final LedgerProcessingService ledgerProcessingService;


    @KafkaListener(
            topics = "payment-events",
            groupId = "ledger-group"
    )
    public void consume(PaymentEvent event) {

        log.info("Received Event : {}", event);

        ledgerProcessingService.processPayment(event);
    }

}