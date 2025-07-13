package com.bank.notification.notification_service.controller;

import com.bank.notification.notification_service.dto.NotificationRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequestDto requestDto) {
        System.out.println(" Notification sent to user ID: " + requestDto.getUserId());
        System.out.println(" Title: " + requestDto.getTitle());
        System.out.println(" Message: " + requestDto.getMsg());
        System.out.println(" Type: " + requestDto.getType());

        return new ResponseEntity<>("Notification sent successfully.", HttpStatus.OK);
    }
}
