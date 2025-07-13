package com.bank.card.CardService.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {

    private LocalDateTime localDateTime;
    private String message;
    private int status;
    private String path;
}
//why this @PreAuthorize("hasRole('USER')") nesary in issuecard
// , here both admin
// and user can acces some api then why its need to define