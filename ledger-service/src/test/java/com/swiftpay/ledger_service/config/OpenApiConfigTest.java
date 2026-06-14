package com.swiftpay.ledger_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    void shouldCreateOpenApiBean() {

        OpenApiConfig config = new OpenApiConfig();

        OpenAPI openAPI = config.swiftPayApi();

        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());

        assertEquals(
                "SwiftPay Ledger Service API",
                openAPI.getInfo().getTitle()
        );

        assertEquals(
                "1.0",
                openAPI.getInfo().getVersion()
        );

        assertEquals(
                "Event Driven Payment Processing Platform",
                openAPI.getInfo().getDescription()
        );

        assertEquals(
                "Sai Varun",
                openAPI.getInfo().getContact().getName()
        );
    }
}