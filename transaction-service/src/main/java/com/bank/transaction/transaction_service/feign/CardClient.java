package com.bank.transaction.transaction_service.feign;

import com.bank.transaction.transaction_service.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "card-service", url = "http://localhost:8082",configuration = FeignClientConfig.class)
public interface CardClient {

    @GetMapping("/api/cards/is-valid/{cardId}")
    boolean isCardValid(@PathVariable Long cardId);

    @GetMapping("/api/cards/type/{cardId}")
    String getCardType(@PathVariable Long cardId); // Returns "DEBIT" or "CREDIT"

}

//This assumes CardMS has an endpoint:
//GET /api/cards/is-valid/{cardId} → returns true if card is active and valid
