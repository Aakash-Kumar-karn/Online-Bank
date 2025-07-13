package com.bank.loan.loan_service.feign;

import com.bank.loan.loan_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "transaction-service",
             url = "http://localhost:8083",
             configuration = FeignClientConfig.class)
public interface TransactionClient {

    @PostMapping("/api/transactions/settle-loan")
    void settleLoan(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam Double amount
    );
}
