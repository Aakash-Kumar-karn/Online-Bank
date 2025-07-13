package com.bank.card.CardService.dto;

import com.bank.card.CardService.model.CardType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDto {

    private Long id;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Card type is required")
    private CardType cardType;

    @NotBlank(message = "Card holder name is required")
    private String cardHolderName;

    @NotNull(message = "Card limit must be specified")
    @Min(value = 1000, message = "Minimum limit must be ₹1000")
    private Double cardLimit;
}
