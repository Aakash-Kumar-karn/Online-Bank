package com.bank.card.CardService.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; //  ID of the user from UserMicroservice

    @Enumerated(EnumType.STRING)
    private CardType cardType; // CREDIT or DEBIT

// Why do we need userId in the Card entity?
// Because it connects the card to a specific user.

    private String cardHolderName;

    private String cardNumber;

    private Double cardLimit;

    private LocalDate expiryDate;

    private String pin; // should be stored hashed later

    private boolean isBlocked = false;
}
