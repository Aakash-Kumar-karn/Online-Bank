package com.bank.card.CardService.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetPinRequestDto {

    @NotNull(message = "Card ID is required")
    private Long cardId;

    @NotBlank(message = "PIN is required")
    @Pattern(regexp = "\\d{4}", message = "PIN must be a 4-digit number")
    private String pin;
}
