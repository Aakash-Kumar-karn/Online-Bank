package com.bank.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableMethodSecurity  // Enables @PreAuthorize support
@EnableDiscoveryClient
@SpringBootApplication
public class UserMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserMicroserviceApplication.class, args);
	}
//spring.application.name=user-service
//server.port=8081
//
//spring.datasource.url=jdbc:mysql://localhost:3308/userdb
//spring.datasource.username=root
//spring.datasource.password=root
//
//spring.jpa.hibernate.ddl-auto=update
//spring.jpa.show-sql=true
//
//
//# Eureka client configuration
//eureka.client.service-url.defaultZone=http://localhost:8761/eureka
//eureka.instance.prefer-ip-address=true
//
//jwt.secret=9ecb1e2eab144b2591234567890123456789012345678901234567890abcd
//
//logging.level.org.springframework.security=DEBUG
//
//#logging.level.org.apache.coyote.http11=DEBUG
//#logging.level.kotlinx.coroutines=ERROR
//
//#//when not register
//eureka.client.register-with-eureka=true
//eureka.client.fetch-registry=true
//eureka.instance.health-check-url-path=/actuator/health
//
//# Enable health endpoint
//management.endpoints.web.exposure.include=health
//management.endpoint.health.show-details=always
}
