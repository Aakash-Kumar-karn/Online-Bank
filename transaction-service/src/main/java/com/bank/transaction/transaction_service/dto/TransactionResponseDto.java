package com.bank.transaction.transaction_service.dto;

import com.bank.transaction.transaction_service.model.Transaction;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    private Long id;
    private Double amount;
    private Transaction.TransactionType type;
    private String description;
    private LocalDateTime timestamp;
    private Double balanceAfterTransaction;
}
