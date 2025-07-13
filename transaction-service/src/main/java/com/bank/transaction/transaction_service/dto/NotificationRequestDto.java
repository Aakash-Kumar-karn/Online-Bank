package com.bank.transaction.transaction_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto {

    private Long userId;
    private String title;
    private String msg;
    private String type; // email ya sms

}
//This DTO will be sent from TransactionMS to NotificationMS via Feign.

