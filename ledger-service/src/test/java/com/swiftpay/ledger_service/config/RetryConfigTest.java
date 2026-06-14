package com.swiftpay.ledger_service.config;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.junit.jupiter.api.Assertions.*;

class RetryConfigTest {

    @Test
    void shouldCreateTaskScheduler() {

        RetryConfig retryConfig = new RetryConfig();

        ThreadPoolTaskScheduler scheduler =
                retryConfig.taskScheduler();

        assertNotNull(scheduler);
        assertEquals(
                "retry-scheduler-",
                scheduler.getThreadNamePrefix()
        );
    }
}