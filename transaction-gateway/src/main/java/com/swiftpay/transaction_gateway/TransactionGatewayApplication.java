package com.swiftpay.transaction_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

import java.util.TimeZone;

@SpringBootApplication
@EnableKafkaRetryTopic
public class TransactionGatewayApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(TransactionGatewayApplication.class, args);
	}
}