package com.bank.transaction.transaction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TransactionMicroserviceApplication {

	public static void main(String[] args) {

		SpringApplication.run(TransactionMicroserviceApplication.class, args);
	}

}
