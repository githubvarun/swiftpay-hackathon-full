package com.swiftpay.transaction_gateway.config;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import static org.junit.jupiter.api.Assertions.*;

class RetryConfigTest {

    private final RetryConfig retryConfig =
            new RetryConfig();

    @Test
    void shouldCreateTaskSchedulerBean() {

        ThreadPoolTaskScheduler scheduler =
                retryConfig.taskScheduler();

        assertNotNull(scheduler);
    }

    @Test
    void shouldSetThreadNamePrefix() {

        ThreadPoolTaskScheduler scheduler =
                retryConfig.taskScheduler();

        assertEquals(
                "retry-scheduler-",
                scheduler.getThreadNamePrefix()
        );
    }

    @Test
    void shouldInitializeScheduler() {

        ThreadPoolTaskScheduler scheduler =
                retryConfig.taskScheduler();

        assertNotNull(
                scheduler.getScheduledThreadPoolExecutor()
        );
    }
}