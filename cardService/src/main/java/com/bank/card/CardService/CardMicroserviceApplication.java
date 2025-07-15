package com.bank.card.CardService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class CardMicroserviceApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(CardMicroserviceApplication.class, args);
	}
}
