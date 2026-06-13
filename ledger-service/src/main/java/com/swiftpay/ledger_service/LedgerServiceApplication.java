package com.swiftpay.ledger_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaRetryTopic;

import java.util.TimeZone;

@SpringBootApplication
//@EnableKafkaRetryTopic
public class LedgerServiceApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		SpringApplication.run(LedgerServiceApplication.class, args);
	}

}
