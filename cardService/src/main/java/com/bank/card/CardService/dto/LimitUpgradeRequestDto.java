package com.bank.card.CardService.dto;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LimitUpgradeRequestDto {

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Requested limit is required")
    @Min(value = 1000, message = "Minimum upgrade must be ₹1000")
    private Double requestedLimit;

    @NotBlank(message = "Reason is required")
    private String reason;
}
