package com.bank.transaction.transaction_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String description;

    private LocalDateTime timestamp;

    private Double balanceAfterTransaction;

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        SHOP_NET_BANKING,
        SHOP_DEBIT_CARD,
        SHOP_CREDIT_CARD
    }
}
