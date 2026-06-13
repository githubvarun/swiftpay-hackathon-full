package com.swiftpay.ledger_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI swiftPayApi() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("SwiftPay Ledger Service API")
                                .version("1.0")
                                .description(
                                        "Event Driven Payment Processing Platform"
                                )
                                .contact(
                                        new Contact()
                                                .name("Sai Varun")
                                )
                );
    }
}