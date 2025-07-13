package com.bank.transaction.transaction_service.feign;

import com.bank.transaction.transaction_service.dto.NotificationRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", url = "http://localhost:8085") // Example port
public interface NotificationClient {
    @PostMapping("/api/notifications/send")
    void sendNotification(@RequestBody NotificationRequestDto dto);
}

//This assumes NotificationMS has /api/notifications/send POST
// endpoint that accepts NotificationRequestDto.