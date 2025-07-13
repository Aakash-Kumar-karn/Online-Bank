package com.bank.transaction.transaction_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/users/check/{userId}")
    boolean checkUserExists(@PathVariable Long userId);
}

// This assumes you’ll create /api/users/check/{id} endpoint in UserMS that
// returns true/false based on user existence.