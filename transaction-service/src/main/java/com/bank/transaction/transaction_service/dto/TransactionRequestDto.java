package com.bank.transaction.transaction_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDto {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Transaction type is required")
    private String type; // we'll map string to enum

    private Long cardId; // Optional, only used for CARD_SHOP

}

//Adding private Long cardId; in TransactionRequestDto
// will NOT affect other features negatively

//You're not putting any validation like @NotNull, so:
//For Deposit, Withdraw, Net Banking:
//cardId is ignored
//No effect, because it's not used in those methods