package com.swiftpay.ledger_service.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KafkaTopicsTest {

    @Test
    void constantsShouldExist() {

        assertEquals(
                "payment-events",
                KafkaTopics.PAYMENT_TOPIC
        );

        assertEquals(
                "payment-result-events",
                KafkaTopics.PAYMENT_RESULT_TOPIC
        );
    }
}