package com.swiftpay.transaction_gateway.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    private final OpenApiConfig config =
            new OpenApiConfig();

    @Test
    void swiftPayApi_shouldCreateOpenApiBean() {

        OpenAPI openAPI =
                config.swiftPayApi();

        assertNotNull(openAPI);
        assertEquals(
                "SwiftPay Transaction Gateway API",
                openAPI.getInfo().getTitle()
        );
        assertEquals(
                "1.0",
                openAPI.getInfo().getVersion()
        );
    }
}